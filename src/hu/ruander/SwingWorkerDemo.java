package hu.ruander;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class SwingWorkerDemo {

  private static final String RESET = "Reset";
  private static final String START = "Start";
  private JFrame frame;
  private JButton btnStart;
  private JTextArea taProgress;
  private JProgressBar progressBar;
  SwingWorker<Void, String> worker;
  private JScrollPane scrollPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          SwingWorkerDemo window = new SwingWorkerDemo();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public SwingWorkerDemo() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 450, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new GridLayout(3, 0, 0, 0));

    // Progress bar
    progressBar = new JProgressBar();
    frame.getContentPane().add(progressBar);

    // Button
    btnStart = new JButton(SwingWorkerDemo.START);
    btnStart.setFont(new Font("Tahoma", Font.BOLD, 20));
    btnStart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (btnStart.getText().equals(SwingWorkerDemo.START)) {
          worker = new SwingWorker<Void, String>() {

            @Override
            protected Void doInBackground() throws Exception {
              for (int i = 0; i < 100; i++) {
                publish(i + ". steps");
                setProgress(i);
                Thread.sleep(30);
              }
              return null;
            }

            @Override
            protected void process(List<String> chunks) {
              for (String chunk : chunks) {
                taProgress.append(chunk + "\n");
              }
            }
          };

          worker.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
              if (evt.getPropertyName().equals("progress")) {
                progressBar.setValue((int) evt.getNewValue());
              }
            }
          });
          worker.execute();
          btnStart.setText(SwingWorkerDemo.RESET);
        } else {
          worker.cancel(true);
          progressBar.setValue(progressBar.getMinimum());
          btnStart.setText(SwingWorkerDemo.START);
          taProgress.setText(null);
        }
      }
    });
    frame.getContentPane().add(btnStart);

    // Text area
    taProgress = new JTextArea();
    scrollPane = new JScrollPane(taProgress);
    frame.getContentPane().add(scrollPane);
  }
}
