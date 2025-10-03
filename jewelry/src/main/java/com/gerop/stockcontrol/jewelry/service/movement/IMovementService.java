package com.gerop.stockcontrol.jewelry.service.movement;

import com.gerop.stockcontrol.jewelry.model.entity.Composition;
import com.gerop.stockcontrol.jewelry.model.entity.Jewel;

public interface IMovementService {
    void jewelRegister(Jewel jewel);
    void modifyJewel(Jewel jewel);
    void jewelDeregister(Jewel jewel);
    void addStock(Jewel jewel);
    void sale(Jewel jewel, Long quantity);
    void inflow(Float quantity, Composition composition);
    void outflow(Float quantity, Composition composition);
    void pendingJewelRestock(Jewel jewel, Long quantity);
}
