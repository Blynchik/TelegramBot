package ru.relex.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "raw_data")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class RawData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "jsonb")
    //сохраняем это поле в формате jsonb
    //для работы с ним ставили ту непонятную зависимость
    //jsonb - оптимизированный двоичный формат json
    //пробелы удаляются, сортировка внутри не сохраняется
    //не сохраняются ключи и дубликаты, последний ключ перезаписывается
    //но этот формат хранится не в виде строки, а виде
    //json'a, по которому можно навигироватьсяс помощью запросов
    //из коропки не поодерживается этот тип данных
    //поэтому навесили эти аннотации
    //и TypeDef выше.
    // за правильное функционирование этого формата
    //отвечает та новая зависимость
    @Column(columnDefinition = "jsonb")
    private Update event;
}
