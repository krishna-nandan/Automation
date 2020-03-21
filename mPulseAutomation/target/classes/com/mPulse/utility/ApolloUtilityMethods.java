package com.mPulse.utility;

import com.mPulse.factories.mPulseDB;
import org.testng.Assert;

public class ApolloUtilityMethods {

    public static void softDeleteCustomFieldFromDataBase (String fieldId) throws Exception {

        int result = mPulseDB.updateData("update custom_field set deleted = true where id = " + fieldId);
        Assert.assertEquals(result, 1);
        String deleteFlag = mPulseDB.getStringData("select deleted from custom_field where id = " + fieldId);
        Assert.assertEquals(deleteFlag, "t");
    }
}
