/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hahmogeneraattori.dao;

/**
 *
 * @author sampo
 */
public interface SettingsDao {

    void setIntValue(String setting, int value);

    void setDoubleValue(String setting, double value);

    int getIntValue(String setting);

    double getDoubleValue(String setting);

    void update() throws Exception;
}
