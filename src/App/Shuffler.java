package App;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Shuffler {
    private final ArrayList<FolderShuffleInfo> folders;
    private int currentFolder;
    private boolean randomize = false;

    public Shuffler() {
        this.folders = new ArrayList<>();
        currentFolder = 0;
    }

    public File getShuffledImagesNext() {
        FolderShuffleInfo temp = folders.get(currentFolder);
        currentFolder = randomize ? new Random().nextInt(folders.size()) : (currentFolder + 1)%folders.size();
        int imageIndex = randomize ? new Random().nextInt(temp.getSize()) : (temp.getCurrent() + 1)%temp.getSize();

        File toReturn = temp.getImage(imageIndex);
        temp.setCurrent(imageIndex);

        return toReturn;
    }

    public File getShuffledImagesPrev() {
        FolderShuffleInfo temp = folders.get(currentFolder);
        currentFolder = randomize ? new Random().nextInt(folders.size()) : currentFolder - 1;
        currentFolder = currentFolder == -1 ? 0 : currentFolder;
        int imageIndex = randomize ? new Random().nextInt(temp.getSize()) : temp.getCurrent() - 1;

        File toReturn = temp.getImage(imageIndex);
        temp.setCurrent(imageIndex == -1 ? temp.getSize()-1 : imageIndex);

        return toReturn;
    }

    public File getContinuousImagesNext() {
        FolderShuffleInfo temp = folders.get(currentFolder);
        currentFolder = randomize ? new Random().nextInt(folders.size()) : currentFolder;
        int imageIndex = randomize ? new Random().nextInt(temp.getSize()) : (temp.getCurrent() + 1)%temp.getSize();

        File toReturn = temp.getImage(imageIndex);
        temp.setCurrent(imageIndex);

        return toReturn;
    }

    public File getContinuousImagesPrev() {
        FolderShuffleInfo temp = folders.get(currentFolder);
        currentFolder = randomize ? new Random().nextInt(folders.size()) : currentFolder;
        int imageIndex = randomize ? new Random().nextInt(temp.getSize()) : temp.getCurrent() - 1;

        temp.setCurrent(imageIndex == -1 ? temp.getSize()-1 : imageIndex);

        return temp.getImage(temp.getCurrent());
    }

    public boolean randomize() {
        randomize = !randomize;
        return randomize;
    }

    public void addImage(int folderIndex, File file) {
        folders.get(folderIndex).addImage(file);
    }

    public void addImage(File file) {
        folders.get(folders.size()-1).addImage(file);
    }

    public void addNewFolder() {
        folders.add(new FolderShuffleInfo());
    }

    public void clearImages(int index) {
        folders.get(index).clearImages();
    }

    public int getCurrentFolder() {
        return currentFolder;
    }

    public boolean isEmpty() {
        return folders.isEmpty();
    }
}
