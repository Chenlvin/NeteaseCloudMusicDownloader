public class Song {
    private String id;
    private String name;
    private String artist;

    public Song(String id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " - " + artist;
    }
}
