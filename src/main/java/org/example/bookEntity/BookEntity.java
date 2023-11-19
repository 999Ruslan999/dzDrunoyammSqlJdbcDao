package org.example.bookEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity {
    private int id;
    private String title;
    private String year;
    private int countSrt;
    private int author_id ;
}
