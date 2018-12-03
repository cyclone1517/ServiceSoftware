package team.hnuwt.meter.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.meter.dao.DataDao;
import team.hnuwt.meter.model.Data;
import team.hnuwt.meter.model.Meter;

public class DataTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        Data data = new Data(1, 1, 123, 1);
        new DataDao().insert(data);
    }

}
