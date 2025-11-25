import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Music Pro - Desktop Edition (Java Swing)
 * Chuyển đổi từ phiên bản Web sang cấu trúc Java Desktop.
 */
public class MusicPro extends JFrame {

    // --- Màu sắc giao diện (Dựa trên hệ màu CSS của bạn) ---
    private final Color PRIMARY = new Color(0, 242, 255);
    private final Color BG_DARK = new Color(10, 12, 16);
    private final Color PANEL_BG = new Color(30, 33, 40);
    private final Color TEXT_MAIN = new Color(230, 237, 243);
    private final Color PK_COLOR = new Color(255, 71, 87);

    // --- State (Dữ liệu ứng dụng) ---
    private String currentUser = null;
    private List<Member> members = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<MusicTab> tabs = new ArrayList<>();

    // --- UI Components ---
    private JPanel gridContainer;
    private JTextField searchField;
    private JLabel statusLabel;

    public MusicPro() {
        initData();
        setupWindow();
        showLogin();
    }

    private void initData() {
        // Khởi tạo các tab mặc định như bản web
        tabs.add(new MusicTab("pk", "PK Tracks", "#ff4757", "PK"));
        tabs.add(new MusicTab("ftx", "FTX Remix", "#1e90ff", "FTX"));
    }

    private void setupWindow() {
        setTitle("Music Pro - Desktop Cloud");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());
    }

    // --- GIAO DIỆN ĐĂNG NHẬP ---
    private void showLogin() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BG_DARK);
        
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(PANEL_BG);
        box.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("MUSIC PRO");
        title.setForeground(PRIMARY);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField userIn = new JTextField(15);
        JPasswordField passIn = new JPasswordField(15);
        JButton loginBtn = new JButton("ĐĂNG NHẬP");
        
        styleButton(loginBtn, PRIMARY, Color.BLACK);

        loginBtn.addActionListener(e -> {
            String user = userIn.getText();
            if(!user.isEmpty()) {
                currentUser = user;
                showMainApp();
            }
        });

        box.add(title);
        box.add(Box.createVerticalStrut(20));
        box.add(new JLabel("Tên đăng nhập:"));
        box.add(userIn);
        box.add(Box.createVerticalStrut(10));
        box.add(new JLabel("Mật khẩu:"));
        box.add(passIn);
        box.add(Box.createVerticalStrut(20));
        box.add(loginBtn);

        loginPanel.add(box);
        setContentPane(loginPanel);
        revalidate();
    }

    // --- GIAO DIỆN CHÍNH ---
    private void showMainApp() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 20, 25));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel(currentUser.toUpperCase() + "'S PRO");
        logo.setForeground(PRIMARY);
        logo.setFont(new Font("SansSerif", Font.BOLD, 18));

        searchField = new JTextField(20);
        searchField.setToolTipText("Tìm kiếm...");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { renderGrid(); }
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton addMemberBtn = new JButton("+ Thành viên");
        JButton addTabBtn = new JButton("+ Tab");
        JButton syncBtn = new JButton("☁ Lưu Cloud");
        
        styleButton(syncBtn, new Color(34, 197, 94), Color.WHITE);

        addMemberBtn.addActionListener(e -> openMemberDialog());
        addTabBtn.addActionListener(e -> openTabDialog());
        syncBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đã đồng bộ với Cloud thành công!"));

        actions.add(addMemberBtn);
        actions.add(addTabBtn);
        actions.add(syncBtn);

        header.add(logo, BorderLayout.WEST);
        header.add(searchField, BorderLayout.CENTER);
        header.add(actions, BorderLayout.EAST);

        // Grid Container
        gridContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        gridContainer.setBackground(BG_DARK);
        JScrollPane scrollPane = new JScrollPane(gridContainer);
        scrollPane.setBorder(null);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
        renderGrid();
        revalidate();
    }

    private void renderGrid() {
        gridContainer.removeAll();
        String query = searchField.getText().toLowerCase();

        // Cột Thành viên
        gridContainer.add(createPanel("👥 THÀNH VIÊN", members.stream()
            .filter(m -> m.name.toLowerCase().contains(query))
            .map(m -> m.name)
            .collect(Collectors.toList()), Color.WHITE));

        // Cột Database
        gridContainer.add(createPanel("🏠 DATABASE", songs.stream()
            .filter(s -> s.name.toLowerCase().contains(query) || s.member.toLowerCase().contains(query))
            .map(s -> s.name + " (" + s.member + ")")
            .collect(Collectors.toList()), PRIMARY));

        // Các cột Tab động
        for (MusicTab tab : tabs) {
            List<String> tabSongs = songs.stream()
                .filter(s -> s.name.toUpperCase().contains(tab.keyword.toUpperCase()))
                .map(s -> s.name)
                .collect(Collectors.toList());
            gridContainer.add(createPanel(tab.name.toUpperCase(), tabSongs, Color.decode(tab.color)));
        }

        gridContainer.revalidate();
        gridContainer.repaint();
    }

    private JPanel createPanel(String title, List<String> items, Color titleColor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(300, 600));
        p.setBackground(PANEL_BG);
        p.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 60)));

        JLabel lbl = new JLabel(title);
        lbl.setForeground(titleColor);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(lbl, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        items.forEach(model::addElement);
        JList<String> list = new JList<>(model);
        list.setBackground(PANEL_BG);
        list.setForeground(TEXT_MAIN);
        
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        return p;
    }

    // --- DIALOGS (Thay thế Modals trong Web) ---
    private void openMemberDialog() {
        String name = JOptionPane.showInputDialog(this, "Nhập tên thành viên:");
        if(name != null && !name.isEmpty()) {
            members.add(new Member(name));
            renderGrid();
        }
    }

    private void openTabDialog() {
        JTextField nameF = new JTextField();
        JTextField keyF = new JTextField();
        Object[] message = { "Tên Tab:", nameF, "Từ khóa:", keyF };
        int option = JOptionPane.showConfirmDialog(null, message, "Tạo Tab mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            tabs.add(new MusicTab(String.valueOf(System.currentTimeMillis()), nameF.getText(), "#00f2ff", keyF.getText()));
            renderGrid();
        }
    }

    // --- HELPERS ---
    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- DATA MODELS ---
    class Member {
        String name;
        Member(String n) { this.name = n; }
    }

    class Song {
        String name, member, url;
        Song(String n, String m, String u) { this.name = n; this.member = m; this.url = u; }
    }

    class MusicTab {
        String id, name, color, keyword;
        MusicTab(String i, String n, String c, String k) {
            this.id = i; this.name = n; this.color = c; this.keyword = k;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MusicPro().setVisible(true);
        });
    }
}