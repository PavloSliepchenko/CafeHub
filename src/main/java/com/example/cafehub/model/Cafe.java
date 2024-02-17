package com.example.cafehub.model;

import jakarta.persistence.CollectionTable;
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
import java.time.LocalTime;
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
    private LocalTime openFromWeekdays;
    @Column(nullable = false)
    private LocalTime closeAtWeekdays;
    private LocalTime openFromWeekends;
    private LocalTime closeAtWeekends;
    @Column(name = "score")
    private BigDecimal score;
    private BigDecimal numberOfUsersVoted;
    @Column(name = "total_score")
    private BigDecimal totalScore;
    @Column(name = "url_to_google_maps")
    private String urlToGoogleMaps;
    private String urlOfImage;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cafe", fetch = FetchType.EAGER)
    private List<Comment> comments;
    private String webSite;
    @Column(nullable = false)
    private boolean coworking = false;
    @Column(nullable = false)
    private boolean vegan = false;
    @Column(nullable = false)
    private boolean petFriendly = false;
    private BigDecimal averageBill;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "images")
    @CollectionTable(name = "images", joinColumns = @JoinColumn(name = "cafe_id"))
    private List<String> images;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cafes_cuisines",
            joinColumns = @JoinColumn(name = "cafe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "cuisines_id", referencedColumnName = "id")
    )
    private List<Cuisine> cuisines;
    @Column(nullable = false)
    private boolean fastService = false;
    @Column(nullable = false)
    private boolean wifi = false;
    @Column(nullable = false)
    private boolean businessLunch = false;
    @Column(nullable = false)
    private boolean freeWater = false;
    @Column(nullable = false)
    private boolean boardGames = false;
    @Column(nullable = false)
    private boolean birthday = false;
    @Column(nullable = false)
    private boolean businessMeeting = false;
    @Column(nullable = false)
    private boolean childHoliday = false;
    @Column(nullable = false)
    private boolean romantic = false;
    @Column(nullable = false)
    private boolean thematicEvent = false;
    @Column(nullable = false)
    private boolean familyHoliday = false;
    @Column(nullable = false)
    private boolean parking = false;
    @Column(nullable = false)
    private boolean terrace = false;
    private String description;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
