package example.org.springboot.domain.posts;

import example.org.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//롬복: 필수 어노테이션은 아니므로 좀더 멀리
@Getter  //클래스 내 모든 필드의 Getter 메소드 자동 생성
@NoArgsConstructor //기본 생성자 자동 추가
//주요 어노테이션을 클래스에 가깝게
@Entity //테이블과 링크될 클래스
public class Posts extends BaseTimeEntity {

    @Id //해당 테이블의 PK 필드 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 생성규칙
    private Long id;

    @Column(length = 500, nullable = false) //테이블 칼럼 , 기본값 외에 추가로 변경된 옵셥이 필요한 경우 사용
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder //해당 클래스의 빌더 패턴 클래스 생성
            //생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    public Posts(String title, String content, String author){ //생성자
        this.title=title;
        this.content=content;
        this.author=author;
    }

    public void update(String title, String content){
        this.title=title;
        this.content=content;
    }
}
