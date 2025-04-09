package ui;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TT2 {

	private JFrame frame;
	private JTable addedDrugTable;
	private JTextField drugInputField;
	private JButton addButton, submitButton;
	private JList<String> historyList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TT2 window = new TT2();
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
	public TT2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("藥物交互系統");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// 左側病患日期藥物添加檔案列表
		historyList = new JList<>();
		JScrollPane historyPane = new JScrollPane(historyList);
		historyPane.setBounds(10, 10, 250, 540);
		frame.getContentPane().add(historyPane);

		// 病患資料顯示
		JTextArea patientInfo = new JTextArea("病患資訊");
		patientInfo.setBounds(270, 10, 500, 80);
		patientInfo.setLineWrap(true);
		patientInfo.setWrapStyleWord(true);
		patientInfo.setEditable(false);
		frame.getContentPane().add(patientInfo);

		// 藥品輸入區
		drugInputField = new JTextField();
		drugInputField.setBounds(270, 100, 400, 30);
		frame.getContentPane().add(drugInputField);

		addButton = new JButton("確認添加");
		addButton.setBounds(680, 100, 90, 30);
		frame.getContentPane().add(addButton);
		addButton.addActionListener(e -> {
		    String drugName = drugInputField.getText().trim();

		    if (drugName.isEmpty()) {
		        JOptionPane.showMessageDialog(frame, "請輸入藥品名稱");
		        return;
		    }

		    DefaultTableModel model = (DefaultTableModel) addedDrugTable.getModel();
		    model.addRow(new Object[]{drugName});
		    drugInputField.setText("");
		});

		// 已添加藥品表格
		addedDrugTable = new JTable(new DefaultTableModel(new Object[] {"藥品名稱"}, 0));
		JScrollPane tablePane = new JScrollPane(addedDrugTable);
		tablePane.setBounds(270, 140, 500, 360);
		frame.getContentPane().add(tablePane);
		addedDrugTable.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        if (evt.getClickCount() == 2) {  // 雙擊刪除
		            int row = addedDrugTable.getSelectedRow();
		            if (row != -1) {
		                DefaultTableModel model = (DefaultTableModel) addedDrugTable.getModel();
		                model.removeRow(row);
		            }
		        }
		    }
		});

		// 送出按鈕
		submitButton = new JButton("送出");
		submitButton.setBounds(670, 510, 100, 40);
		frame.getContentPane().add(submitButton);
		submitButton.addActionListener(e -> {
<<<<<<< HEAD
		    JOptionPane.showMessageDialog(frame, "警訊觸發!!!：Na+ < 120 mEq/l, Hyponatramia (NAL)", "警訊通知", JOptionPane.WARNING_MESSAGE);
=======
		    JOptionPane.showMessageDialog(frame, "⚠️ 警訊觸發：Na+ < 120 mEq/l, Hyponatramia (NAL)", "警訊通知", JOptionPane.WARNING_MESSAGE);
>>>>>>> f4d72502ab26bca9b522a349786921d818e80c70
		});
		
	}
	
	private void submitLabResult(int pid, String key, float value, String source) {
	    try (Connection conn = DBConnection.getConnection()) {

	        // 1. 插入 lab 結果
	        String insertLabSql = "INSERT INTO patient_lab_results (pid, record_time, key_name, value, source) VALUES (?, NOW(), ?, ?, ?)";
	        PreparedStatement insertPs = conn.prepareStatement(insertLabSql);
	        insertPs.setInt(1, pid);
	        insertPs.setString(2, key);
	        insertPs.setFloat(3, value);
	        insertPs.setString(4, source);
	        insertPs.executeUpdate();

	        // 2. 比對 alarm_rules 表中觸發邏輯
	        String alarmSql = "SELECT trigger_condition, alarm_message FROM alarm_rules";
	        PreparedStatement alarmPs = conn.prepareStatement(alarmSql);
	        ResultSet rs = alarmPs.executeQuery();

	        while (rs.next()) {
	            String condition = rs.getString("trigger_condition");
	            String message = rs.getString("alarm_message");

	            // 基礎範例，只處理單純條件 like Na+ < 120
	            if (condition.contains(key)) {
	                if (condition.contains("<")) {
	                    float threshold = Float.parseFloat(condition.replaceAll("[^0-9.]", ""));
	                    if (value < threshold) {
	                        JOptionPane.showMessageDialog(null, "⚠️ 警訊觸發：" + message + "（" + key + " = " + value + "）");
	                        return;
	                    }
	                } else if (condition.contains(">")) {
	                    float threshold = Float.parseFloat(condition.replaceAll("[^0-9.]", ""));
	                    if (value > threshold) {
	                        JOptionPane.showMessageDialog(null, "⚠️ 警訊觸發：" + message + "（" + key + " = " + value + "）");
	                        return;
	                    }
	                }
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "送出 Lab 結果時發生錯誤！");
	    }
	}
	
	
}
