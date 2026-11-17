import java.util.Scanner;
class Song {
    String songName;
    String artistName;

    Song(String songName, String artistName) {
        this.songName = songName;
        this.artistName = artistName;
    }

    void displaySong() {
        System.out.println(songName + " - " + artistName);
    }
}

public class MusicPlaylist {

    static final int SIZE = 10;

    Song[] queue = new Song[SIZE];

    int front = -1;
    int rear = -1;
    int count = 0;

    // Check empty
    boolean isEmpty() {
        return count == 0;
    }

    // Check full
    boolean isFull() {
        return count == SIZE;
    }

    // Duplicate policy: same song name AND same artist (ignoring case) is rejected
    boolean contains(String name, String artist) {
        for (int i = 0; i < count; i++) {
            Song s = queue[(front + i) % SIZE];
            if (s.songName.equalsIgnoreCase(name)
                    && s.artistName.equalsIgnoreCase(artist)) {
                return true;
            }
        }
        return false;
    }

    // Add a song at the rear
    void enqueue(String name, String artist) {

        if (isFull()) {
            System.out.println("Playlist is Full!");
            return;
        }

        if (contains(name, artist)) {
            System.out.println("Duplicate song! It is already in the playlist.");
            return;
        }

        if (isEmpty()) {
            front = 0;
        }

        rear = (rear + 1) % SIZE;
        queue[rear] = new Song(name, artist);
        count++;

        System.out.println("Song added successfully!");
    }

    // Remove the current (front) song
    void dequeue() {

        if (isEmpty()) {
            System.out.println("Playlist is Empty!");
            return;
        }

        System.out.println("Removed Song: " + queue[front].songName);

        queue[front] = null;
        count--;

        if (count == 0) {
            front = -1;
            rear = -1;
        } else {
            front = (front + 1) % SIZE;
        }

        System.out.println("Song removed successfully!");
    }

    // Play next song: current song goes to the rear, next one becomes front
    void playNext() {

        if (isEmpty()) {
            System.out.println("No songs in playlist!");
            return;
        }

        if (count == 1) {
            System.out.println("Only one song in playlist!");
            queue[front].displaySong();
            return;
        }

        Song current = queue[front];
        queue[front] = null;                // free the old front slot

        front = (front + 1) % SIZE;
        rear = (rear + 1) % SIZE;
        queue[rear] = current;               // current song joins the rear

        System.out.println("\nNow Playing:");
        queue[front].displaySong();
    }

    //  last song in the queue moves to the front
    void playPrevious() {

        if (isEmpty()) {
            System.out.println("No songs in playlist!");
            return;
        }

        if (count == 1) {
            System.out.println("Only one song in playlist!");
            queue[front].displaySong();
            return;
        }

        Song last = queue[rear];
        queue[rear] = null;                  // free the old rear slot

        rear = (rear - 1 + SIZE) % SIZE;
        front = (front - 1 + SIZE) % SIZE;
        queue[front] = last;                 // last song becomes the front

        System.out.println("\nNow Playing:");
        queue[front].displaySong();
    }

    // Display playlist from current song onwards
    void displayPlaylist() {

        if (isEmpty()) {
            System.out.println("Playlist is Empty!");
            return;
        }

        System.out.println("\n===== YOUR PLAYLIST =====");

        for (int i = 0; i < count; i++) {
            int index = (front + i) % SIZE;
            System.out.print((i + 1) + ". ");
            queue[index].displaySong();
        }
    }

    // Show current song
    void showCurrentSong() {

        if (isEmpty()) {
            System.out.println("No song is currently playing!");
            return;
        }

        System.out.println("\nCurrently Playing:");
        queue[front].displaySong();
    }

    // Show internal array state (useful for dry run and viva)
    void showInternalState() {

        System.out.println("\n===== INTERNAL ARRAY STATE =====");

        for (int i = 0; i < SIZE; i++) {
            String marker = "";
            if (i == front) marker += " <- front";
            if (i == rear) marker += " <- rear";

            String content = (queue[i] == null)
                    ? "[empty]"
                    : queue[i].songName + " - " + queue[i].artistName;

            System.out.println("Index " + i + ": " + content + marker);
        }

        System.out.println("front = " + front + ", rear = " + rear + ", count = " + count);
    }

    // Main method
    public static void main(String[] args) {

        MusicPlaylist mp = new MusicPlaylist();
        Scanner sc = new Scanner(System.in);

        int choice = 0;

        do {

            System.out.println("\n================================");
            System.out.println("     MUSIC PLAYLIST ROTATION");
            System.out.println("================================");

            System.out.println("1. Add Song");
            System.out.println("2. Remove Current Song");
            System.out.println("3. Play Next Song");
            System.out.println("4. Play Previous Song");
            System.out.println("5. Display Playlist");
            System.out.println("6. Show Currently Playing");
            System.out.println("7. Check Playlist Status");
            System.out.println("8. Show Internal Array State");
            System.out.println("9. Exit");

            System.out.print("\nEnter your choice: ");

            // Stop cleanly if input ends (e.g. piped input)
            if (!sc.hasNextLine()) {
                break;
            }

            String line = sc.nextLine().trim();

            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter a number from 1 to 9.");
                choice = 0;
                continue;
            }

            switch (choice) {

                case 1:
                    System.out.print("Enter Song Name: ");
                    String name = sc.hasNextLine() ? sc.nextLine().trim() : "";

                    System.out.print("Enter Artist Name: ");
                    String artist = sc.hasNextLine() ? sc.nextLine().trim() : "";

                    if (name.isEmpty() || artist.isEmpty()) {
                        System.out.println("Song name and artist cannot be empty!");
                    } else {
                        mp.enqueue(name, artist);
                    }
                    break;

                case 2:
                    mp.dequeue();
                    break;

                case 3:
                    mp.playNext();
                    break;

                case 4:
                    mp.playPrevious();
                    break;

                case 5:
                    mp.displayPlaylist();
                    break;

                case 6:
                    mp.showCurrentSong();
                    break;

                case 7:
                    System.out.println("Playlist Empty: " + mp.isEmpty());
                    System.out.println("Playlist Full: " + mp.isFull());
                    System.out.println("Total Songs: " + mp.count);
                    break;

                case 8:
                    mp.showInternalState();
                    break;

                case 9:
                    System.out.println("Exiting Music Playlist...");
                    break;

                default:
                    System.out.println("Invalid choice! Enter a number from 1 to 9.");
            }

        } while (choice != 9);

        sc.close();
    }
}
