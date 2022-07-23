package cleanbook.com.entity.page;

import cleanbook.com.entity.enums.SettingType;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@DynamicInsert
@Embeddable
public class PageSetting {

    @Column(columnDefinition = "boolean default false")
    private boolean noticeLike;

    @Column(columnDefinition = "boolean default false")
    private boolean noticeComment;

    @Column(columnDefinition = "varchar(20) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private SettingType readAuth;

    @Column(columnDefinition = "boolean default false")
    private boolean CommentReadAuth;

    @Column(columnDefinition = "boolean default false")
    private boolean CommentWriteAuth;

    @Column(columnDefinition = "boolean default false")
    private boolean LikeReadAuth;


}
