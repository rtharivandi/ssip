package App;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FolderShuffleInfo {
    private final ArrayList<File> images = new ArrayList<>();
    private int current;

    public FolderShuffleInfo() {
        current = 0;
    }

    public File getImage(int index) {
        return images.get(index);
    }

    public void addImage(File file) {
        if (file.isDirectory())
            images.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        else
            images.add(file);
        images.removeIf(this::isNotImage);
    }

    public void clearImages() {
        images.clear();
    }

    private boolean isNotImage(File file) {
        try {
            return !Files.probeContentType(file.toPath()).split("/")[0].equals("image");
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return images.size();
    }

    public boolean isEmpty() {
        return images.isEmpty();
    }

    public String getParentFile() {
        return images.get(0).getParent();
    }
}
