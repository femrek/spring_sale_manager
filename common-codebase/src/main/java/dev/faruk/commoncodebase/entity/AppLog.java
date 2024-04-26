package dev.faruk.commoncodebase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * the only Entity object for storing log entries.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "app_log", schema = "public")
public class AppLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "url")
    private String url;

    @Column(name = "method")
    private String method;

    @Column(name = "request")
    private String requestBody;

    @Column(name = "response")
    private String response;

    @Column(name = "error")
    private String error;

    @Column(name = "stack_trace")
    private String stackTrace;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "request_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestAt;

    @Column(name = "response_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseAt;
}
