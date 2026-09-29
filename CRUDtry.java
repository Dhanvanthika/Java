import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CRUDtry extends JFrame {

    static final String URL = "jdbc:mysql://localhost:3306/javatry";
    static final String USER = "root";
    static final String PASSWORD = "ChristBanglore2025";

    JTextField idField, titleField, authorField, priceField;
    JTable table;
    DefaultTableModel model;
    Connection con;

    CRUDtry() {

        // Connect to MySQL
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            Statement stmt = con.createStatement();

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INT PRIMARY KEY, " +
                "title VARCHAR(100), " +
                "author VARCHAR(100), " +
                "price DOUBLE)"
            );

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Database connection failed!\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Window
        setTitle("E-Library Management");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        mainPanel.setBackground(new Color(245, 240, 250));

        // TITLE 

        JLabel heading = new JLabel(
            "📚 E-Library Management",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(new Color(90, 60, 100));

        // FORM

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(new Color(245, 240, 250));

        idField = new JTextField();
        titleField = new JTextField();
        authorField = new JTextField();
        priceField = new JTextField();

        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);

        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        // BUTTONS

        JButton addButton = new JButton("Add Book");
        JButton viewButton = new JButton("View Books");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        addButton.setBackground(new Color(180, 220, 190));
        viewButton.setBackground(new Color(190, 210, 235));
        updateButton.setBackground(new Color(245, 220, 170));
        deleteButton.setBackground(new Color(240, 190, 190));
        clearButton.setBackground(new Color(220, 200, 230));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 240, 250));

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // TOP SECTION  

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(245, 240, 250));

        topPanel.add(heading, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // TABLE

        model = new DefaultTableModel(
            new String[]{"Book ID", "Title", "Author", "Price"}, 0
        );

        table = new JTable(model);

        table.setRowHeight(28);
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // BUTTON ACTIONS 

        addButton.addActionListener(e -> addBook());

        viewButton.addActionListener(e -> displayBooks());

        updateButton.addActionListener(e -> updateBook());

        deleteButton.addActionListener(e -> deleteBook());

        clearButton.addActionListener(e -> clearFields());

        // ROW SELECTION

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = table.getSelectedRow();

                if (row != -1) {

                    idField.setText(
                        model.getValueAt(row, 0).toString()
                    );

                    titleField.setText(
                        model.getValueAt(row, 1).toString()
                    );

                    authorField.setText(
                        model.getValueAt(row, 2).toString()
                    );

                    priceField.setText(
                        model.getValueAt(row, 3).toString()
                    );
                }
            }
        });

        add(mainPanel);

        setVisible(true);

        // Show existing books when application opens
        displayBooks();
    }

    // CREATE

    void addBook() {

        try {

            if (idField.getText().trim().isEmpty() ||
                titleField.getText().trim().isEmpty() ||
                authorField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields!"
                );
                return;
            }

            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String author = authorField.getText();
            double price = Double.parseDouble(priceField.getText());

            String sql =
                "INSERT INTO books VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setDouble(4, price);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Book added successfully! 📖"
            );

            displayBooks();
            clearFields();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Book ID and Price must be numbers!"
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Could not add book.\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // READ

    void displayBooks() {

        try {

            model.setRowCount(0);

            String sql = "SELECT * FROM books";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getDouble("price")
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Could not display books.\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // UPDATE

    void updateBook() {

        try {

            if (idField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Select a book from the table first!"
                );
                return;
            }

            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String author = authorField.getText();
            double price = Double.parseDouble(priceField.getText());

            String sql =
                "UPDATE books SET title = ?, author = ?, price = ? " +
                "WHERE book_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setDouble(3, price);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Book updated successfully! ✨"
                );

                displayBooks();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Book not found!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Book ID and Price must be numbers!"
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Could not update book.\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // DELETE

    void deleteBook() {

        try {

            if (idField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Select a book from the table first!"
                );
                return;
            }

            int id = Integer.parseInt(idField.getText());

            String sql =
                "DELETE FROM books WHERE book_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Book deleted successfully! 🗑"
                );

                displayBooks();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Book not found!"
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Could not delete book.\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CLEAR

    void clearFields() {

        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        priceField.setText("");

        table.clearSelection();
    }

    // MAIN 

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new CRUDtry();
        });
    }
}