# 藥量警訊系統 (Alarm-based-on-rules)

## 📖 介紹

本系統是一個基於 Java Swing 的桌面應用程式，主要用於管理病患的藥品歷史記錄，並檢查新添加藥品之間的交互作用。

## ✨✨✨ 功能添加
- ✅ **藥量警訊警告**：當送出藥品時，系統會檢查是否有藥量過多，並給出警告。

![image](https://github.com/user-attachments/assets/bc4f3eb7-fc7c-45b2-9978-6451ce7219a2)

## 🛠 環境需求

- 🔹 **JDK 8 或以上**
- 🔹 **MySQL 數據庫**
- 🔹 **JDBC 驅動** (確保 `mysql-connector-java` 已導入到專案中)


## 🗄 數據庫設計

```sql
CREATE TABLE alarm_rules (
    id INT PRIMARY KEY AUTO_INCREMENT,
    trigger_condition VARCHAR(255),
    alarm_message VARCHAR(100)
);


CREATE TABLE patient_lab_results (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pid INT,
    record_time DATETIME,
    key_name VARCHAR(50),
    value FLOAT,
    source VARCHAR(20)
);

```

## 🔍 檢查藥量警告邏輯

```java
private void submitLabResult(int pid, String key, float value, String source) {
	    try (Connection conn = DBConnection.getConnection()) {

	        String insertLabSql = "INSERT INTO patient_lab_results (pid, record_time, key_name, value, source) VALUES (?, NOW(), ?, ?, ?)";
	        PreparedStatement insertPs = conn.prepareStatement(insertLabSql);
	        insertPs.setInt(1, pid);
	        insertPs.setString(2, key);
	        insertPs.setFloat(3, value);
	        insertPs.setString(4, source);
	        insertPs.executeUpdate();

	        String alarmSql = "SELECT trigger_condition, alarm_message FROM alarm_rules";
	        PreparedStatement alarmPs = conn.prepareStatement(alarmSql);
	        ResultSet rs = alarmPs.executeQuery();

	        while (rs.next()) {
	            String condition = rs.getString("trigger_condition");
	            String message = rs.getString("alarm_message");

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
```

