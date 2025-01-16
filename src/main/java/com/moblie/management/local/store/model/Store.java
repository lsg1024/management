package com.moblie.management.local.store.model;

import com.moblie.management.global.utils.BaseEntity;
import com.moblie.management.local.store.dto.StoreDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE STORE SET deleted = true where store_id = ?")
@SQLRestriction("deleted = false")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    @Column(unique = true)
    private String storeName;

    @Column(unique = true)
    private String storePhoneNumber;
    private boolean deleted = Boolean.FALSE;

    protected Store(String storeName, String storePhoneNumber) {
        this.storeName = storeName;
        this.storePhoneNumber = storePhoneNumber;
    }

    public static Store create(StoreDto.commonDto storeDto) {
        return new Store(
                storeDto.getStoreName(),
                storeDto.getStorePhone());
    }

    public void update(StoreDto.commonDto updateDto) {
        this.storeName = updateDto.getStoreName();
        this.storePhoneNumber = updateDto.getStorePhone();
    }

    public void delete(String userId) {
        if (!this.getCreatedBy().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        this.deleted = true;
    }

}
