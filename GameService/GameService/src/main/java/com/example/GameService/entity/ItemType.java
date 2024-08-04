package com.example.GameService.entity;

// ItemType.java
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "item_types")
@Data
@Getter
@Setter
public class ItemType {
    @Id
    private String id;
    private Long itemTypeID;
    private Long eventID;

    private Long gameID;
    private String nameOfItem;
    private String picture;
    private String type;

    public Long getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(Long itemTypeID) {
        this.itemTypeID = itemTypeID;
    }
}
