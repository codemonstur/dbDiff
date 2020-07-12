package dbdiff.pojos;

import dbdiff.pojos.db.ColumnType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ColumnTypeTest {

    /**
     * Test null type name
     */
    @Test
    public void testNullTypeName() {
        ColumnType typeBool = new ColumnType(-7, null);
        ColumnType typeBool2 = new ColumnType(-7, null);
        assertEquals("Expected bool types are equal", typeBool, typeBool2);
    }

    /**
     * Test two ColumnType objects are equal
     */
    @Test
    public void testEquals() {
        ColumnType typeBool = new ColumnType(-7, "bool");
        ColumnType typeBoolean = new ColumnType(16, "boolean");
        ColumnType typeBool2 = new ColumnType(-7, "bool");
        assertEquals("Expected bool types are equal", typeBool, typeBool2);
        assertNotSame("Expected bool and boolean are not equal", typeBool, typeBoolean);
    }

    /**
     * Test ColumnType object can be used as key
     */
    @Test
    public void testEquality() {
        Map<ColumnType, ColumnType> typeMap = new HashMap<ColumnType, ColumnType>();
        ColumnType typeBool = new ColumnType(-7, "bool");
        ColumnType typeBoolean = new ColumnType(16, "boolean");
        ColumnType typeBool2 = new ColumnType(-7, "bool");
        typeMap.put(typeBool, typeBool2);
        typeMap.put(typeBoolean, typeBool2);
        assertEquals("Expected bool types are equal", typeBool, typeMap.get(typeBool));
        assertEquals("Expected bool types are equal", typeBool, typeMap.get(typeBoolean));
    }
}
