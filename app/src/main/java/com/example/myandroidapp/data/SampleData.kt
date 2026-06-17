package com.example.myandroidapp.data

enum class Language {
    ENGLISH,
    HINDI
}

data class BilingualText(
    val english: String,
    val hindi: String
) {
    fun get(lang: Language): String = if (lang == Language.ENGLISH) english else hindi
}

enum class PresenceStatus {
    PRESENT,   // 🟢 Available
    MEETING,   // 🟡 In Meeting
    ABSENT     // 🔴 Out of Office
}

data class Officer(
    val id: String,
    val name: BilingualText,
    val designation: BilingualText,
    val room: String,
    val status: PresenceStatus,
    val lastUpdated: BilingualText,
    val drawableName: String = ""
)

data class RouteStep(
    val stepNumber: Int,
    val title: BilingualText,
    val description: BilingualText,
    val directionIcon: String // "straight", "right", "left", "up_stairs", "elevator"
)

data class Service(
    val id: String,
    val name: BilingualText,
    val description: BilingualText,
    val documentsRequired: List<BilingualText>,
    val routeSteps: List<RouteStep>,
    val estimatedMinutes: Int
)

data class Department(
    val id: String,
    val name: BilingualText,
    val officers: List<Officer>,
    val services: List<Service>
)

data class Building(
    val id: String,
    val name: BilingualText,
    val description: BilingualText,
    val address: BilingualText,
    val latitude: Double,
    val longitude: Double,
    val departments: List<Department>,
    val drawableName: String = ""
)

object SampleData {
    val buildings = listOf(
        Building(
            id = "collector_office",
            name = BilingualText("District Collector Office", "जिला कलेक्टर कार्यालय"),
            description = BilingualText(
                "Main administrative center for district administration, certificates, and land records.",
                "जिला प्रशासन, प्रमाण पत्र और भूमि रिकॉर्ड के लिए मुख्य प्रशासनिक केंद्र।"
            ),
            address = BilingualText("Civil Lines, Sector 4, City Center", "सिविल लाइंस, सेक्टर 4, सिटी सेंटर"),
            latitude = 21.1458,
            longitude = 79.0882,
            drawableName = "building_collector",
            departments = listOf(
                Department(
                    id = "revenue",
                    name = BilingualText("Revenue Department", "राजस्व विभाग"),
                    officers = listOf(
                        Officer(
                            id = "off_1",
                            name = BilingualText("Mr. Rajesh Kumar Patil", "श्री राजेश कुमार पाटिल"),
                            designation = BilingualText("District Collector & Magistrate", "जिला कलेक्टर और मजिस्ट्रेट"),
                            room = "Cabin 101, First Floor",
                            status = PresenceStatus.MEETING,
                            lastUpdated = BilingualText("Updated 10m ago", "10 मिनट पहले अपडेट किया गया"),
                            drawableName = "officer_patil"
                        ),
                        Officer(
                            id = "off_2",
                            name = BilingualText("Mrs. Sneha Lata Joshi", "श्रीमती स्नेहा लता जोशी"),
                            designation = BilingualText("Deputy Collector (Revenue)", "उप कलेक्टर (राजस्व)"),
                            room = "Cabin 104, First Floor",
                            status = PresenceStatus.PRESENT,
                            lastUpdated = BilingualText("Updated 2m ago", "2 मिनट पहले अपडेट किया गया"),
                            drawableName = "officer_joshi"
                        )
                    ),
                    services = listOf(
                        Service(
                            id = "domicile",
                            name = BilingualText("Domicile Certificate", "अधिवास (मूल निवासी) प्रमाण पत्र"),
                            description = BilingualText(
                                "Proof of permanent residence within the state, required for admissions and jobs.",
                                "राज्य के भीतर स्थायी निवास का प्रमाण, प्रवेश और नौकरियों के लिए आवश्यक।"
                            ),
                            estimatedMinutes = 15,
                            documentsRequired = listOf(
                                BilingualText("Aadhaar Card (identity proof)", "आधार कार्ड (पहचान प्रमाण)"),
                                BilingualText("Ration Card / Electricity Bill (address proof)", "राशन कार्ड / बिजली बिल (पता प्रमाण)"),
                                BilingualText("School Leaving Certificate (birth place proof)", "स्कूल छोड़ने का प्रमाण पत्र (जन्म स्थान प्रमाण)"),
                                BilingualText("Affidavit of Residence", "निवास का हलफनामा (शपथ पत्र)")
                            ),
                            routeSteps = listOf(
                                RouteStep(
                                    stepNumber = 1,
                                    title = BilingualText("Enter the Main Gate", "मुख्य द्वार में प्रवेश करें"),
                                    description = BilingualText("Enter through the main arch gate and undergo security screening.", "मुख्य मेहराबदार प्रवेश द्वार से अंदर जाएं और सुरक्षा जांच कराएं।"),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 2,
                                    title = BilingualText("Go to the Central Lobby", "केंद्रीय लॉबी में जाएं"),
                                    description = BilingualText("Walk 30 meters straight into the lobby. You will see a directory board.", "लॉबी में सीधे 30 मीटर चलें। आपको एक निर्देश बोर्ड दिखाई देगा।"),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 3,
                                    title = BilingualText("Turn Right into Block A", "ब्लॉक ए में दाएं मुड़ें"),
                                    description = BilingualText("Turn right from the help desk. Walk past the waiting area.", "सहायता डेस्क से दाईं ओर मुड़ें। प्रतीक्षालय के पास से आगे चलें।"),
                                    directionIcon = "right"
                                ),
                                RouteStep(
                                    stepNumber = 4,
                                    title = BilingualText("Counter 3 on the Left", "बाईं ओर काउंटर 3"),
                                    description = BilingualText("Go to Counter 3 (Revenue Services). Submit your file here.", "काउंटर 3 (राजस्व सेवाएं) पर जाएं। अपनी फाइल यहां जमा करें।"),
                                    directionIcon = "left"
                                )
                            )
                        ),
                        Service(
                            id = "income",
                            name = BilingualText("Income Certificate", "आय प्रमाण पत्र"),
                            description = BilingualText(
                                "Official certificate showing annual income, useful for scholarships and fee waivers.",
                                "वार्षिक आय दर्शाने वाला आधिकारिक प्रमाण पत्र, छात्रवृत्ति और शुल्क छूट के लिए उपयोगी।"
                            ),
                            estimatedMinutes = 20,
                            documentsRequired = listOf(
                                BilingualText("Aadhaar Card", "आधार कार्ड"),
                                BilingualText("Ration Card", "राशन कार्ड"),
                                BilingualText("Salary Slip (if employed) or Land Revenue Receipt", "वेतन पर्ची (यदि कार्यरत हैं) या भू-राजस्व रसीद"),
                                BilingualText("Declaration of Income signed by Talathi / Patwari", "तलाठी / पटवारी द्वारा हस्ताक्षरित आय की घोषणा")
                            ),
                            routeSteps = listOf(
                                RouteStep(
                                    stepNumber = 1,
                                    title = BilingualText("Enter the Main Gate", "मुख्य द्वार में प्रवेश करें"),
                                    description = BilingualText("Enter and pass the security check at the entrance.", "प्रवेश द्वार पर प्रवेश करें और सुरक्षा जांच पास करें।"),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 2,
                                    title = BilingualText("Head to First Floor Stairs", "पहली मंजिल की सीढ़ियों की ओर बढ़ें"),
                                    description = BilingualText("Walk straight and take the stairs on the left behind the elevator.", "सीधे चलें और लिफ्ट के पीछे बाईं ओर की सीढ़ियों से जाएं।"),
                                    directionIcon = "up_stairs"
                                ),
                                RouteStep(
                                    stepNumber = 3,
                                    title = BilingualText("Go to Room 104", "कमरा नंबर 104 में जाएं"),
                                    description = BilingualText("At the top of the stairs, turn left and walk 15 meters. Room 104 is on your right.", "सीढ़ियों के शीर्ष पर, बाईं ओर मुड़ें और 15 मीटर चलें। कमरा 104 आपकी दाईं ओर है।"),
                                    directionIcon = "right"
                                )
                            )
                        )
                    )
                ),
                Department(
                    id = "land_records",
                    name = BilingualText("Land Records (Bhumi Abhilekh)", "भूमि रिकॉर्ड (भूमी अभिलेख)"),
                    officers = listOf(
                        Officer(
                            id = "off_3",
                            name = BilingualText("Mr. Anand P. Shinde", "श्री आनंद पी. शिंदे"),
                            designation = BilingualText("Superintendent of Land Records", "भूमि रिकॉर्ड के अधीक्षक"),
                            room = "Cabin 205, Second Floor",
                            status = PresenceStatus.ABSENT,
                            lastUpdated = BilingualText("Updated 1h ago", "1 घंटे पहले अपडेट किया गया"),
                            drawableName = "officer_shinde"
                        )
                    ),
                    services = listOf(
                        Service(
                            id = "extract_712",
                            name = BilingualText("7/12 Extract (७/१२ उतारा)", "७/१२ उतारा (भूमि रिकॉर्ड)"),
                            description = BilingualText(
                                "Document containing details of land ownership, cultivation, and agricultural loans.",
                                "भूमि स्वामित्व, खेती और कृषि ऋण का विवरण देने वाला दस्तावेज।"
                            ),
                            estimatedMinutes = 10,
                            documentsRequired = listOf(
                                BilingualText("Survey Number / Gut Number of the Land", "भूमि का सर्वेक्षण नंबर / गुट नंबर"),
                                BilingualText("Aadhaar Card of the applicant", "आवेदक का आधार कार्ड"),
                                BilingualText("Application Form", "आवेदन पत्र")
                            ),
                            routeSteps = listOf(
                                RouteStep(
                                    stepNumber = 1,
                                    title = BilingualText("Enter the Main Gate", "मुख्य द्वार में प्रवेश करें"),
                                    description = BilingualText("Enter through the main gate.", "मुख्य द्वार से प्रवेश करें।"),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 2,
                                    title = BilingualText("Take the Elevator", "लिफ्ट लें"),
                                    description = BilingualText("Walk straight, the elevator is in the center of the lobby. Select Floor 2.", "सीधे चलें, लिफ्ट लॉबी के केंद्र में है। मंजिल 2 चुनें।"),
                                    directionIcon = "elevator"
                                ),
                                RouteStep(
                                    stepNumber = 3,
                                    title = BilingualText("Go to Land Records Section", "भूमि रिकॉर्ड अनुभाग में जाएं"),
                                    description = BilingualText("Exit the elevator, turn left, and enter Room 205 at the end of the hall.", "लिफ्ट से बाहर निकलें, बाईं ओर मुड़ें, और हॉल के अंत में कमरा 205 में प्रवेश करें।"),
                                    directionIcon = "left"
                                )
                            )
                        )
                    )
                )
            )
        ),
        Building(
            id = "municipal_corp",
            name = BilingualText("Municipal Corporation", "महानगरपालिका / नगर निगम"),
            description = BilingualText(
                "City civic center handling birth/death registration, property tax, and water supply issues.",
                "शहर का नागरिक केंद्र जो जन्म/मृत्यु पंजीकरण, संपत्ति कर और जलापूर्ति के मुद्दों को संभालता है।"
            ),
            address = BilingualText("Mahatma Gandhi Road, Opp. Central Park", "महात्मा गांधी रोड, सेंट्रल पार्क के सामने"),
            latitude = 21.1498,
            longitude = 79.0806,
            drawableName = "building_municipal",
            departments = listOf(
                Department(
                    id = "health",
                    name = BilingualText("Health & Vital Statistics", "स्वास्थ्य एवं जन्म-मृत्यू विभाग"),
                    officers = listOf(
                        Officer(
                            id = "off_4",
                            name = BilingualText("Mr. Vinay D. Deshmukh", "श्री विनय डी. देशमुख"),
                            designation = BilingualText("Registrar of Births & Deaths", "जन्म और मृत्यु के रजिस्ट्रार"),
                            room = "Room 12, Ground Floor",
                            status = PresenceStatus.PRESENT,
                            lastUpdated = BilingualText("Updated 5m ago", "5 मिनट पहले अपडेट किया गया"),
                            drawableName = "officer_deshmukh"
                        )
                    ),
                    services = listOf(
                        Service(
                            id = "birth_cert",
                            name = BilingualText("Birth Certificate", "जन्म प्रमाण पत्र"),
                            description = BilingualText(
                                "Official registration of a child's birth, essential for school admissions.",
                                "बच्चे के जन्म का आधिकारिक पंजीकरण, स्कूल में प्रवेश के लिए आवश्यक।"
                            ),
                            estimatedMinutes = 25,
                            documentsRequired = listOf(
                                BilingualText("Hospital Discharge Card / Birth Report", "अस्पताल डिस्चार्ज कार्ड / जन्म रिपोर्ट"),
                                BilingualText("Aadhaar Card of Mother and Father", "माता और पिता का आधार कार्ड"),
                                BilingualText("Marriage Certificate (Optional)", "विवाह प्रमाण पत्र (वैकल्पिक)")
                            ),
                            routeSteps = listOf(
                                RouteStep(
                                    stepNumber = 1,
                                    title = BilingualText("Enter Civic Center Hall", "नागरिक केंद्र हॉल में प्रवेश करें"),
                                    description = BilingualText("Walk past the reception desk near the entrance.", "प्रवेश द्वार के पास रिसेप्शन डेस्क से आगे चलें।"),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 2,
                                    title = BilingualText("Go to Health Section Corridor", "स्वास्थ्य अनुभाग कॉरिडोर में जाएं"),
                                    description = BilingualText("Turn left at the reception, walk down the corridor to Room 12.", "रिसेप्शन पर बाईं ओर मुड़ें, कॉरिडोर से होते हुए कमरा 12 में जाएं।"),
                                    directionIcon = "left"
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}
