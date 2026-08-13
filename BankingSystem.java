import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class BankingSystem extends JFrame {

    // Account class
    static class Account {
        String accountNumber;
        String name;
        String pin;
        double balance;

        Account(String accountNumber, String name, String pin, double balance) {
            this.accountNumber = accountNumber;
            this.name = name;
            this.pin = pin;
            this.balance = balance;
        }
    }

    private final Map<String, Account> accounts = new HashMap<>();
    private Account currentAccount;

    // Colors
    private final Color PRIMARY = new Color(25, 118, 210);
    private final Color DARK = new Color(30, 40, 50);
    private final Color LIGHT = new Color(245, 247, 250);

    public BankingSystem() {
        setTitle("SmartBank - Banking System");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Demo account
        accounts.put("1001",
                new Account("1001", "Demo User", "1234", 10000));

        showLoginScreen();
    }

    // ---------------- LOGIN SCREEN ----------------
    private void showLoginScreen() {
        getContentPane().removeAll();

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT);

        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY);
        header.setPreferredSize(new Dimension(850, 110));

        JLabel title = new JLabel("SMARTBANK");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 34));

        JLabel subtitle = new JLabel("Secure Digital Banking");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(Box.createVerticalStrut(20));
        header.add(title);
        header.add(subtitle);

        // Login panel
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        loginPanel.setLayout(new GridLayout(5, 1, 10, 10));

        JLabel loginTitle = new JLabel("Account Login", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField accountField = new JTextField();
        JPasswordField pinField = new JPasswordField();

        accountField.setBorder(
                BorderFactory.createTitledBorder("Account Number"));
        pinField.setBorder(
                BorderFactory.createTitledBorder("PIN"));

        JButton loginButton = createButton("LOGIN", PRIMARY);
        JButton createButton = createButton("CREATE ACCOUNT", new Color(46, 125, 50));

        loginPanel.add(loginTitle);
        loginPanel.add(accountField);
        loginPanel.add(pinField);
        loginPanel.add(loginButton);
        loginPanel.add(createButton);

        loginButton.addActionListener(e -> {
            String accNo = accountField.getText().trim();
            String pin = new String(pinField.getPassword());

            Account account = accounts.get(accNo);

            if (account != null && account.pin.equals(pin)) {
                currentAccount = account;
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid account number or PIN!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        createButton.addActionListener(e -> showCreateAccount());

        main.add(header, BorderLayout.NORTH);
        main.add(loginPanel, BorderLayout.CENTER);

        setContentPane(main);
        revalidate();
        repaint();
    }

    // ---------------- CREATE ACCOUNT ----------------
    private void showCreateAccount() {
        getContentPane().removeAll();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create New Account");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JTextField accountNo = new JTextField();
        JTextField name = new JTextField();
        JPasswordField pin = new JPasswordField();
        JTextField initialDeposit = new JTextField();

        accountNo.setBorder(
                BorderFactory.createTitledBorder("Account Number"));
        name.setBorder(
                BorderFactory.createTitledBorder("Account Holder Name"));
        pin.setBorder(
                BorderFactory.createTitledBorder("4-Digit PIN"));
        initialDeposit.setBorder(
                BorderFactory.createTitledBorder("Initial Deposit"));

        JButton create = createButton("CREATE ACCOUNT", PRIMARY);
        JButton back = createButton("BACK", DARK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        panel.add(accountNo, gbc);

        gbc.gridx = 1;
        panel.add(name, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(pin, gbc);

        gbc.gridx = 1;
        panel.add(initialDeposit, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(create, gbc);

        gbc.gridx = 1;
        panel.add(back, gbc);

        create.addActionListener(e -> {
            String acc = accountNo.getText().trim();
            String customer = name.getText().trim();
            String password = new String(pin.getPassword());

            if (acc.isEmpty() || customer.isEmpty() ||
                    password.length() != 4) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid account details.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (accounts.containsKey(acc)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Account number already exists!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                double deposit =
                        Double.parseDouble(initialDeposit.getText());

                if (deposit < 0) {
                    throw new NumberFormatException();
                }

                accounts.put(
                        acc,
                        new Account(acc, customer, password, deposit)
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Account created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                showLoginScreen();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid deposit amount.",
                        "Invalid Amount",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        back.addActionListener(e -> showLoginScreen());

        setContentPane(panel);
        revalidate();
        repaint();
    }

    // ---------------- DASHBOARD ----------------
    private void showDashboard() {
        getContentPane().removeAll();

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setPreferredSize(new Dimension(850, 80));

        JLabel welcome = new JLabel(
                "  Welcome, " + currentAccount.name
        );

        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));

        JButton logout = createButton("LOGOUT", DARK);

        header.add(welcome, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        // Balance card
        JPanel balancePanel = new JPanel();
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel balanceLabel = new JLabel(
                String.format("₹ %.2f", currentAccount.balance)
        );

        balanceLabel.setFont(new Font("Arial", Font.BOLD, 36));
        balanceLabel.setForeground(new Color(46, 125, 50));

        JLabel balanceTitle =
                new JLabel("Available Balance",
                        SwingConstants.CENTER);

        balanceTitle.setFont(
                new Font("Arial", Font.BOLD, 18));

        balancePanel.setLayout(
                new BoxLayout(balancePanel, BoxLayout.Y_AXIS));

        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        balanceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        balancePanel.add(balanceTitle);
        balancePanel.add(Box.createVerticalStrut(10));
        balancePanel.add(balanceLabel);

        // Buttons
        JPanel buttonPanel = new JPanel(
                new GridLayout(2, 3, 15, 15));

        buttonPanel.setBackground(LIGHT);
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JButton deposit = createButton(
                "💰  DEPOSIT", new Color(46, 125, 50));

        JButton withdraw = createButton(
                "💸  WITHDRAW", new Color(198, 40, 40));

        JButton balance = createButton(
                "💳  BALANCE", PRIMARY);

        JButton details = createButton(
                "👤  ACCOUNT DETAILS",
                new Color(123, 31, 162));

        JButton transfer = createButton(
                "🔄  TRANSFER",
                new Color(0, 121, 107));

        JButton exit = createButton(
                "EXIT",
                DARK);

        buttonPanel.add(deposit);
        buttonPanel.add(withdraw);
        buttonPanel.add(balance);
        buttonPanel.add(details);
        buttonPanel.add(transfer);
        buttonPanel.add(exit);

        main.add(header, BorderLayout.NORTH);
        main.add(balancePanel, BorderLayout.CENTER);
        main.add(buttonPanel, BorderLayout.SOUTH);

        // Deposit
        deposit.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter deposit amount:"
            );

            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);

                    if (amount > 0) {
                        currentAccount.balance += amount;

                        JOptionPane.showMessageDialog(
                                this,
                                "₹" + amount +
                                        " deposited successfully!"
                        );

                        showDashboard();
                    } else {
                        showError("Amount must be greater than zero.");
                    }

                } catch (NumberFormatException ex) {
                    showError("Enter a valid amount.");
                }
            }
        });

        // Withdraw
        withdraw.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter withdrawal amount:"
            );

            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);

                    if (amount <= 0) {
                        showError(
                                "Amount must be greater than zero."
                        );
                    } else if (amount > currentAccount.balance) {
                        showError("Insufficient balance!");
                    } else {
                        currentAccount.balance -= amount;

                        JOptionPane.showMessageDialog(
                                this,
                                "₹" + amount +
                                        " withdrawn successfully!"
                        );

                        showDashboard();
                    }

                } catch (NumberFormatException ex) {
                    showError("Enter a valid amount.");
                }
            }
        });

        // Balance
        balance.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        String.format(
                                "Current Balance: ₹%.2f",
                                currentAccount.balance
                        ),
                        "Balance",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        // Account details
        details.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Account Number: " +
                                currentAccount.accountNumber +
                                "\nAccount Holder: " +
                                currentAccount.name +
                                "\nBalance: ₹" +
                                String.format(
                                        "%.2f",
                                        currentAccount.balance
                                ),
                        "Account Details",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        // Transfer
        transfer.addActionListener(e -> transferMoney());

        // Logout
        logout.addActionListener(e -> {
            currentAccount = null;
            showLoginScreen();
        });

        exit.addActionListener(e -> System.exit(0));

        setContentPane(main);
        revalidate();
        repaint();
    }

    // ---------------- TRANSFER ----------------
    private void transferMoney() {

        String receiver =
                JOptionPane.showInputDialog(
                        this,
                        "Enter receiver account number:"
                );

        if (receiver == null) return;

        Account target = accounts.get(receiver);

        if (target == null) {
            showError("Receiver account not found.");
            return;
        }

        if (target == currentAccount) {
            showError("You cannot transfer to your own account.");
            return;
        }

        String amountText =
                JOptionPane.showInputDialog(
                        this,
                        "Enter transfer amount:"
                );

        if (amountText == null) return;

        try {
            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                showError("Amount must be greater than zero.");
            } else if (amount > currentAccount.balance) {
                showError("Insufficient balance.");
            } else {
                currentAccount.balance -= amount;
                target.balance += amount;

                JOptionPane.showMessageDialog(
                        this,
                        "Transfer successful!\n\n" +
                                "Amount: ₹" + amount +
                                "\nTo Account: " + receiver,
                        "Transfer Complete",
                        JOptionPane.INFORMATION_MESSAGE
                );

                showDashboard();
            }

        } catch (NumberFormatException ex) {
            showError("Enter a valid amount.");
        }
    }

    // ---------------- BUTTON STYLE ----------------
    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Arial", Font.BOLD, 14));

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // ---------------- ERROR MESSAGE ----------------
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            BankingSystem bank = new BankingSystem();
            bank.setVisible(true);
        });
    }
}