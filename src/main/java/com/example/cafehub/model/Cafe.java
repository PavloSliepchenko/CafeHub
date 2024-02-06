package com.example.cafehub.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name = "cafes")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE cafes SET is_deleted = true WHERE id = ?")
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cafes_languages",
            joinColumns = @JoinColumn(name = "cafe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "language_id", referencedColumnName = "id")
    )
    private List<Language> languages;
    @Column(nullable = false)
    private String workHoursWeekDays;
    @Column(nullable = false)
    private String workHoursWeekends;
    private int score;
    private String urlOfImage;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cafe", fetch = FetchType.EAGER)
    private List<Comment> comments;
    private String webSite;
    private boolean coworking;
    private boolean vegan;
    private boolean petFriendly;
    private BigDecimal averageBill;
    @Column
    @ElementCollection
    private List<String> images;
    private boolean childZone;
    private boolean smokingFriendly;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
