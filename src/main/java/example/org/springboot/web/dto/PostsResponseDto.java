package example.org.springboot.web.dto;

import example.org.springboot.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity){ //여기서는 Entity 필드의 일부만 사용하기 때문에 생성자로 Entity를 받아 필드에 채움
                                        // 굳이 모든 필드를 가진 생성자가 필요하지는 않기 때문에 Dto는 Entity를 받아서 처리
        this.id= entity.getId();
        this.title=entity.getTitle();
        this.content= entity.getContent();
        this.author= entity.getAuthor();
    }
}

