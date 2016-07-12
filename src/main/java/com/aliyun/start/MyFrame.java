package com.aliyun.start;

import com.aliyun.initConfInfo.DescribeDomainRecords;
import com.aliyun.settingJob.UpdateDomainRecordJob;
import com.aliyun.utils.PathUtil;
import org.quartz.SchedulerException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static com.aliyun.initConfInfo.AnalyzeConf.CONF_MAP;
import static com.aliyun.quartz.QuartzManager.addJob;
import static com.aliyun.quartz.QuartzManager.removeJob;
import static com.aliyun.settingJob.UpdateDomainRecordJob.jobKey;
import static com.aliyun.settingJob.UpdateDomainRecordJob.triggerKey;
import static com.aliyun.utils.FileUtil.writeErrToLog;
import static com.aliyun.utils.UTCTimeUtil.getNow;

/**
 * Created by yangf on 2016/7/7.
 */
public class MyFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 600;
    public static final int DEFAULT_HEIGHT = 500;
    public static JTextPane textPane;
    public static JButton startBtn;
    public static JButton stopBtn;
    private TrayIcon trayIcon;//托盘图标
    private SystemTray systemTray;//系统托盘

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        if (args.length != 0 && "start".equals(args[0])) {
            DescribeDomainRecords ddr = new DescribeDomainRecords();
            if (!ddr.execute()) {
                return;
            }
            try {
                String cron = CONF_MAP.get("Cron");
                addJob(UpdateDomainRecordJob.class, jobKey, triggerKey, cron);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return;
        }
        JFrame frame = new MyFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        DescribeDomainRecords ddr = new DescribeDomainRecords();
        if (!ddr.execute()) {
            return;
        }
        startBtn.setEnabled(true);


    }

    public MyFrame() {
        setTitle("Set up the dynamic IP");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        //构建窗口内的元素
        textPane = new JTextPane();
        JPanel panel = new JPanel();
        startBtn = new JButton("Start");
        stopBtn = new JButton("Stop");
        startBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        panel.add(startBtn);
        panel.add(stopBtn);
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        //获得系统托盘的实例
        systemTray = SystemTray.getSystemTray();
        //构造系统托盘
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        try {
            System.out.println(PathUtil.LOGO_ICO);
            Image image = ImageIO.read(new File(PathUtil.LOGO_ICO));
            setIconImage(image);
            trayIcon = new TrayIcon(image);
            systemTray.add(trayIcon);//设置托盘的图标
        } catch (IOException e) {
            writeErrToLog(e,
                getNow() + "[ path load fail ]: please check ( PathUtil.LOGO_ICO )");
        } catch (AWTException e) {
            writeErrToLog(e,
                getNow() + "[ path load fail ]: please check ( PathUtil.LOGO_ICO )");
        }

        addWindowListener(new WindowAdapter() {
            public void windowIconified(WindowEvent e) {
                dispose();//窗口最小化时dispose该窗口
            }
        });

        addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e ) {
                try {
                    removeJob(jobKey, triggerKey);
                } catch (SchedulerException e1) {
                    writeErrToLog(e1, getNow() + "[ remove job fail ]: ", e1.getMessage());
                }finally {
                    System.exit(0);
                }
            }
        });

        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)//双击托盘窗口再现
                    setExtendedState(Frame.NORMAL);
                setVisible(true);
            }
        });
        setLocationRelativeTo(null); //用来计算窗口初始位置居中
        setResizable(false); //设置窗口大小不可变
        //开始按钮执行的方法
        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopBtn.setEnabled(true);
                startBtn.setEnabled(false);
                try {
                    String cron = CONF_MAP.get("Cron");
                    addJob(UpdateDomainRecordJob.class, jobKey, triggerKey, cron);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        //停止按钮执行的方法
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    removeJob(jobKey, triggerKey);
                    stopBtn.setEnabled(false);
                    startBtn.setEnabled(true);
                } catch (SchedulerException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void insertDocument(String text, Color textColor)//根据传入的颜色及文字，将文字插入文本域
    {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textColor);//设置文字颜色
        StyleConstants.setFontSize(set, 12);//设置字体大小
        Document doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text + "\n", set);//插入文字
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}


