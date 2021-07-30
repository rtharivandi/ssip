import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static ArrayList<String> images = new ArrayList<>();
    private static int currentSlide = 0;

    public App() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // Setting the frame of the program
                JFrame frame = new JFrame("SSIP");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Creating the BufferedImage
                BufferedImage bufferedImage = null;
                
               
                    try {
                        bufferedImage = ImageIO.read(new File(images.get(0)));
                    } catch (IOException e) {
                        System.err.println("File error.");
                    }
        
                    ImageIcon imageIcon = new ImageIcon(bufferedImage);
                    JLabel label = new JLabel();
        
                    // Set icon is the command that changes the picture
                    label.setIcon(imageIcon);
        
                    frame.getContentPane().add(label, BorderLayout.CENTER);
                    frame.pack();
        
                    // Centering the frame
                    frame.setLocationRelativeTo(null);
        
                    // Making it visible
                    frame.setVisible(true);

                    // try {
                    //     Thread.sleep(1500);
                    // } catch (InterruptedException e) {
                    //     e.fillInStackTrace();
                    // }
                
                //System.out.println(images.get(0));

                }


        });

        
    }

    public static void nextImage() {
        currentSlide = (currentSlide + 1) % images.size();
    }

    public static void main(String[] args) throws Exception {

        while (true) {
            System.out.println("Please enter a command!");
            System.out.println("p : Play the slideshow.\na : Add a new file.");
            System.out.println("c : Clear the queue\n");

            //Scanner scanner = new Scanner(System.in);
            char command = new Scanner(System.in).next().charAt(0);
            App currentApp = null;

            if (command == 'a') {
                System.out.println("Please enter the path of the pic.");
                String path = new Scanner(System.in).next();
                System.out.println(path);
                images.add(path);
                System.out.println("File succesfully added.");
            } else if (command == 'p') {
                System.out.println(images.get(currentSlide));
                new App();
            } else if (command == 'c') {
                images.clear();
            } else if (command == 'n') {
                nextImage();  
                //new App();
            } else if (command == 'e') {
                return;
            } else {
                System.out.println("Please enter a valid command.\n\n");
            }
        }

    

        //  if (args[0].equals("a")) {
        //     ObjectInputStream in = null;
        //     try {
        //         in = new ObjectInputStream(new FileInputStream("imagecache.ser"));
        //     } finally {
        //         System.out.println(args[1] + "\n");
        //         images.add(args[1]);

        //         ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imagecache.ser"));
        //         out.writeObject(images);
        //         out.flush();
        //         out.close();
        //         System.out.println("File succesfully added!");
        //     }

            
        // }

        // else if (args[0].equals("p")) {
        //    try {
        //     new App();
        //    } catch (IndexOutOfBoundsException e) {
        //        System.err.println("No files loaded.");
        //    }
        // } 

        // else if (args[0].equals("n")) {
        //     if (currentSlide == images.size()-1) {
        //         System.err.println("Cannot go further, please add more pictures.");
        //     } else {
        //         currentSlide++;
        //     }
        // }


        // else if (args[0].equals("am")) {
        //     for (int i = 1; i < args.length; i++) {
        //         try {
        //             images.add(ImageIO.read(new File(args[i])));
        //         } catch (IOException e) {
        //             System.err.println("File " + args[i] + " not found!");
        //         }
        //     }
        // } 

        // else if (args[0].equals("c")) {
        //     images.clear();
        // } 

        // // else if (args[0].equals("s")) {
        // //     for (ImageIcon imageIcon : images) {
        // //         System.out.println(imageIcon.get);
        // //     }
        // // }

        // else {
        //     System.out.println("Please enter a valid command!");
        }

    }

