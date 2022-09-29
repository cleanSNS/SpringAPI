package cleanbook.com.entity.page;

import cleanbook.com.entity.enums.SettingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@DynamicInsert
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageSetting {

    @Column(columnDefinition = "boolean default false")
    private boolean notificationLike;

    @Column(columnDefinition = "boolean default false")
    private boolean notificationComment;

    @Column(columnDefinition = "varchar(20) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private SettingType readAuth;

    @Column(columnDefinition = "boolean default false")
    private boolean commentReadAuth;

    @Column(columnDefinition = "boolean default false")
    private boolean commentWriteAuth;

    @Column(columnDefinition = "boolean default false")
    private boolean likeReadAuth;

    void changePageSetting(PageSetting pageSetting) {
        this.notificationLike = pageSetting.isNotificationLike();
        this.notificationComment = pageSetting.isNotificationComment();
        this.readAuth = pageSetting.getReadAuth();
        this.commentReadAuth = pageSetting.isCommentReadAuth();
        this.commentWriteAuth = pageSetting.isCommentWriteAuth();
        this.likeReadAuth = pageSetting.isLikeReadAuth();
    }
}
