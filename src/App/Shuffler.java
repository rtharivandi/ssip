package App;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Shuffler {
    private final ArrayList<FolderShuffleInfo> folders;
    private int currentFolder;
    private boolean randomize = false;
    private boolean loop = false;

    public Shuffler() {
        this.folders = new ArrayList<>();
        currentFolder = 0;
    }

    public File getShuffledImagesNext() {
        currentFolder = randomize ? new Random().nextInt(folders.size()) : (currentFolder + 1) % folders.size();

        FolderShuffleInfo temp = folders.get(currentFolder);
        int imageIndex = randomize ? new Random().nextInt(temp.getSize()) : (temp.getCurrent() + 1)%temp.getSize();

        temp.setCurrent(imageIndex);
        return temp.getImage(imageIndex);
    }

    public File getShuffledImagesPrev() {
        currentFolder = randomize ? new Random().nextInt(folders.size()) : currentFolder - 1;
        currentFolder = currentFolder == -1 ? folders.size()-1 : currentFolder;

        FolderShuffleInfo temp = folders.get(currentFolder);
        int imageIndex = randomize ? new Random().nextInt(temp.getSize()) : temp.getCurrent() - 1;

        temp.setCurrent(imageIndex == -1 ? temp.getSize()-1 : imageIndex);
        return temp.getImage(temp.getCurrent());
    }

    public File getContinuousImagesNext() {
        currentFolder = randomize ? new Random().nextInt(folders.size()) : currentFolder;

        FolderShuffleInfo temp = folders.get(currentFolder);
        int imageIndex = randomize ? new Random().nextInt(temp.getSize()) : (temp.getCurrent() + 1)%temp.getSize();

        File toReturn = temp.getImage(imageIndex);
        temp.setCurrent(imageIndex);

        if (!loop) {
            if (imageIndex == temp.getSize() - 1)
                currentFolder = (currentFolder + 1) % folders.size();
        }

        return toReturn;
    }

    public File getContinuousImagesPrev() {
        currentFolder = randomize ? new Random().nextInt(folders.size()) : currentFolder;

        FolderShuffleInfo temp = folders.get(currentFolder);
        int imageIndex = randomize ? new Random().nextInt(temp.getSize()) : temp.getCurrent() - 1;

        temp.setCurrent(imageIndex == -1 ? temp.getSize()-1 : imageIndex);

        if (!loop){
            if (imageIndex == 0)
                currentFolder = currentFolder == 0 ? folders.size() - 1 : currentFolder - 1;
        }

        return temp.getImage(temp.getCurrent());
    }

    public boolean randomize() {
        randomize = !randomize;
        return randomize;
    }

    public boolean loop() {
        loop = !loop;
        return loop;
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

    public String clearImages(int index) {
        if (!folders.isEmpty()){
            String parent = folders.get(currentFolder).getParentFile();
            folders.remove(currentFolder);
            return parent;
        }
        return null;
    }

    public int getCurrentFolder() {
        return currentFolder;
    }

    public boolean isEmpty() {
        return folders.isEmpty();
    }

    public int getNumberOfFolders(){
        return folders.size();
    }
}
