package cleanbook.com.domain.user;

import cleanbook.com.domain.SettingType;
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
public class UserNoticeSetting {

    @Column(columnDefinition = "boolean default false")
    private boolean noticeFollow;

    @Column(columnDefinition = "varchar(15) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private SettingType noticeComment;

    @Column(columnDefinition = "varchar(15) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private SettingType noticeLike;

    @Column(columnDefinition = "boolean default false")
    private boolean noticeFollowAccept;

    @Column(columnDefinition = "boolean default false")
    private boolean noticeSingleChat;

    @Column(columnDefinition = "boolean default false")
    private boolean noticeGroupChat;
}
