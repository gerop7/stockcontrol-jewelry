package com.gerop.stockcontrol.jewelry.service.pendingtorestock;

import com.gerop.stockcontrol.jewelry.model.entity.pendingtorestock.PendingRestock;

public interface IPendingRestockService<T extends PendingRestock, N extends Number> {
    T create();
    void addToRestock(Long id, N quantity);
    void removeFromRestock(Long id, N quantity);
}
