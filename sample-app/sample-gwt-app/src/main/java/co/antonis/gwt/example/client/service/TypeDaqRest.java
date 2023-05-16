package co.antonis.gwt.example.client.service;

public enum TypeDaqRest {

    //region Methods Basic
    MethodLogin("basic","login", 2, 3, -1),
    MethodLogout("basic","logout", 3),
    MethodGetLogEntries("basic","getLogEntries", 2),

    MethodGetCommands("basic","getCommands", 4),
    MethodGetCommand("basic","getCommand", 3),
    MethodAddCommand("basic","addCommand", 2),
    MethodGetCommandsCount("basic","getCommandsCount", 2),
    MethodDeleteCommands("basic","deleteCommands", 2),

    MethodGetVesselDefinition("basic","getVesselDefinition", 2),
    MethodGetParameters("basic","getParameters", 2),

    MethodGetVesselStructures("basic","getVesselStructures", 1),
    MethodGetVesselStructureJson("basic","getVesselDefinition", 2),
    MethodUpdateVesselProperties("basic","updateVesselProperties", 2),
    MethodGetVesselRuntimeValues("basic","getVesselRuntimeValues", 2),
    MethodUpdateRuntimeValue("basic","updateRuntimeValue", 5),
    MethodGetVoyageTelegramPeriod("basic","getVoyageTelegramPeriod", 4),

    MethodGetUserStructures("basic","getUserStructures", 4),
    MethodDeleteUser("basic","deleteUser", 3),
    MethodUpdateUser("basic","updateUser", 5),
    MethodUpdateUserPassword("basic","updateUserPassword", 4),

    MethodGetVersion("basic","getVersion", 0),
    MethodGetDatabaseInfo("basic","getDatabaseInfo", 1),
    //endregion

    //region Values
    MethodGetUserMetadata("values","getUserMetadata",3,4,-1),
    MethodGetUserMetadataBasic("values","getUserMetadataBasic",4),
    MethodGetUserMetaDataImportExportJson("values","getUserMetaDataImportExportJson",3),
    MethodUpdateUserMetadataDashboard("values","updateUserMetadataDashboard",2),
    MethodUpdateUserMetadataDiagnostic("values","updateUserMetadataDiagnostic",2),
    MethodDeleteUserMetadata("values","deleteUserMetadata",2),

    MethodGetDiagnosticResult("values","getDiagnosticResult",2),
    MethodGetGPDStructure("values","getGPDStructure",4),
    MethodGetDiagIterStructure("values","getDiagIterStructure",4),

    MethodGetParameterValues1("values","getParameterValues1",4),
    MethodGetParameterValues2("values","getParameterValues2",4),
    MethodGetFormData("values","getFormData",4),
    MethodDeleteParameterValue("values","deleteParameterValue",4),

    MethodSetExportStructure("values","setExportStructure",2);


    //endregion

    String name;
    String category;
    int numOfAcceptedParams1;
    int numOfAcceptedParams2;
    int numOfAcceptedParams3;

    TypeDaqRest(String category, String name, int numOfParams1) {
        this(category, name, numOfParams1, -1, -1);
    }

    TypeDaqRest(String category,String name, int numOfParams1, int numOfParams2, int numOfParams3) {
        this.name = name;
        this.category = category;
        this.numOfAcceptedParams1 = numOfParams1;
        this.numOfAcceptedParams2 = numOfParams2;
        this.numOfAcceptedParams3 = numOfParams3;
    }

    public static TypeDaqRest valueOfSafe(String value) {
        try {
            return TypeDaqRest.valueOf(value);
        } catch (Exception exc) {
            return null;
        }
    }

    public boolean isCategoryBasic(){
        return "basic".equals(this.category);
    }

    public boolean isValid(int params) {
        return params == numOfAcceptedParams1 || params == numOfAcceptedParams2 || params == numOfAcceptedParams3;
    }
}
