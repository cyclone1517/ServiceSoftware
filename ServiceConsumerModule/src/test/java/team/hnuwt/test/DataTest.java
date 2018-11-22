package team.hnuwt.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.bean.Meter;
import team.hnuwt.dao.DataDao;

public class DataTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        Meter meter = new Meter();
        meter.setId(1);
        meter.setData(123);
        meter.setValveState(1);
        new DataDao().insert(meter);
    }
    
    @Test
    public void batchTest() {
        List<Meter> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            Meter meter = new Meter();
            meter.setId(i);
            meter.setData(100 + i);
            meter.setValveState(0);
            list.add(meter);
        } 
        new DataDao().insertBatch(list);
    }

}
