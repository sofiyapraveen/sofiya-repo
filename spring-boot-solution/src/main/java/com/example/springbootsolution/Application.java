package com.example.springbootsolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

/**
 * Runner that executes the SQL query to find the highest salary
 * that was credited to an employee, but only for transactions that were not
 * made on the 1st day of any month. Along with the salary, extracts employee
 * data like name (combined), age and department.
 */
@Component
class StartupRunner implements org.springframework.boot.ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
        System.out.println("Starting SQL solution execution...");

        // Clear previous results
        jdbc.update("DELETE FROM SOLUTION_RESULTS");

        // Execute the main SQL query to find highest salary (excluding 1st day
        // transactions)
        String sql = """
                SELECT
                    p.AMOUNT as SALARY,
                    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME,
                    YEAR(CURRENT_DATE) - YEAR(e.DOB) -
                        CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.DOB) OR
                                  (MONTH(CURRENT_DATE) = MONTH(e.DOB) AND DAY(CURRENT_DATE) < DAY(e.DOB))
                             THEN 1 ELSE 0 END as AGE,
                    d.DEPARTMENT_NAME
                FROM PAYMENTS p
                JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
                JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
                WHERE DAY(p.PAYMENT_TIME) != 1
                ORDER BY p.AMOUNT DESC
                LIMIT 1
                """;

        System.out.println("Executing SQL query: " + sql);

        List<Map<String, Object>> results = jdbc.query(sql, (ResultSet rs, int rowNum) -> {
            Map<String, Object> row = new java.util.HashMap<>();
            row.put("SALARY", rs.getBigDecimal("SALARY"));
            row.put("NAME", rs.getString("NAME"));
            row.put("AGE", rs.getInt("AGE"));
            row.put("DEPARTMENT_NAME", rs.getString("DEPARTMENT_NAME"));
            return row;
        });

        if (!results.isEmpty()) {
            Map<String, Object> result = results.get(0);

            // Store result in SOLUTION_RESULTS table
            jdbc.update("INSERT INTO SOLUTION_RESULTS (SALARY, NAME, AGE, DEPARTMENT_NAME) VALUES (?, ?, ?, ?)",
                    result.get("SALARY"),
                    result.get("NAME"),
                    result.get("AGE"),
                    result.get("DEPARTMENT_NAME"));

            // Display the result
            System.out.println("\n=== HIGHEST SALARY RESULT (Excluding 1st Day Transactions) ===");
            System.out.println("SALARY: $" + result.get("SALARY"));
            System.out.println("NAME: " + result.get("NAME"));
            System.out.println("AGE: " + result.get("AGE"));
            System.out.println("DEPARTMENT: " + result.get("DEPARTMENT_NAME"));
            System.out.println("=============================================================\n");

            // Also display all payment records for reference
            System.out.println("=== ALL PAYMENT RECORDS ===");
            List<Map<String, Object>> allPayments = jdbc.query(
                    "SELECT p.PAYMENT_ID, p.AMOUNT, p.PAYMENT_TIME, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME " +
                            "FROM PAYMENTS p " +
                            "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                            "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                            "ORDER BY p.PAYMENT_TIME",
                    (ResultSet rs, int rowNum) -> {
                        Map<String, Object> row = new java.util.HashMap<>();
                        row.put("PAYMENT_ID", rs.getInt("PAYMENT_ID"));
                        row.put("AMOUNT", rs.getBigDecimal("AMOUNT"));
                        row.put("PAYMENT_TIME", rs.getTimestamp("PAYMENT_TIME"));
                        row.put("NAME", rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME"));
                        row.put("DEPARTMENT", rs.getString("DEPARTMENT_NAME"));
                        return row;
                    });

            for (Map<String, Object> payment : allPayments) {
                System.out.printf("Payment ID: %d, Amount: $%s, Date: %s, Employee: %s, Department: %s%n",
                        payment.get("PAYMENT_ID"),
                        payment.get("AMOUNT"),
                        payment.get("PAYMENT_TIME"),
                        payment.get("NAME"),
                        payment.get("DEPARTMENT"));
            }

        } else {
            System.out.println("No payment records found that meet the criteria (excluding 1st day transactions).");
        }

        System.out.println("\nSQL solution execution completed.");
    }
}
