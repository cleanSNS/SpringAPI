package cleanbook.com.domain.user;

import cleanbook.com.domain.SettingEnum;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@DynamicInsert
@Embeddable
public class UserSetting {

    @Column(columnDefinition = "boolean default false")
    private boolean noticeFollow;

    @Column(columnDefinition = "varchar(15) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private SettingEnum noticeComment;

    @Column(columnDefinition = "varchar(15) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private SettingEnum noticeLike;

    @Column(columnDefinition = "boolean default false")
    private boolean noticeFollowAccept;

    @Column(columnDefinition = "boolean default false")
    private boolean noticeSingleChat;

    @Column(columnDefinition = "boolean default false")
    private boolean noticeGroupChat;

    @Column(columnDefinition = "boolean default false")
    private boolean filterAll;

    @Column(columnDefinition = "boolean default false")
    private boolean filterFollower;

    @Column(columnDefinition = "boolean default false")
    private boolean filterFollowee;
}
