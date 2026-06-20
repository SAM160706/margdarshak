package com.example.myandroidapp.data

enum class Language {
    ENGLISH,
    HINDI,
    MARATHI
}

data class BilingualText(
    val english: String,
    val hindi: String,
    val marathi: String = ""
) {
    fun get(lang: Language): String = when (lang) {
        Language.ENGLISH -> english
        Language.HINDI -> hindi
        Language.MARATHI -> if (marathi.isNotEmpty()) marathi else hindi
    }
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
            name = BilingualText("District Collector Office", "जिला कलेक्टर कार्यालय", "जिल्हाधिकारी कार्यालय"),
            description = BilingualText(
                "Main administrative center for district administration, certificates, and land records.",
                "जिला प्रशासन, प्रमाण पत्र और भूमि रिकॉर्ड के लिए मुख्य प्रशासनिक केंद्र।",
                "जिला प्रशासन, प्रमाणपत्रे आणि जमीन महसूल नोंदींसाठी मुख्य प्रशासकीय केंद्र."
            ),
            address = BilingualText("Civil Lines, Sector 4, City Center", "सिविल लाइंस, सेक्टर 4, सिटी सेंटर", "सिव्हिल लाईन्स, सेक्टर ४, सिटी सेंटर"),
            latitude = 21.1458,
            longitude = 79.0882,
            drawableName = "building_collector",
            departments = listOf(
                Department(
                    id = "revenue",
                    name = BilingualText("Revenue Department", "राजस्व विभाग", "महसूल विभाग"),
                    officers = listOf(
                        Officer(
                            id = "off_1",
                            name = BilingualText("Mr. Rajesh Kumar Patil", "श्री राजेश कुमार पाटिल", "श्री. राजेश कुमार पाटील"),
                            designation = BilingualText("District Collector & Magistrate", "जिला कलेक्टर और मजिस्ट्रेट", "जिल्हाधिकारी आणि जिल्हा दंडाधिकारी"),
                            room = "Cabin 101, First Floor",
                            status = PresenceStatus.MEETING,
                            lastUpdated = BilingualText("Updated 10m ago", "10 मिनट पहले अपडेट किया गया", "१० मिनिटांपूर्वी अपडेट केले"),
                            drawableName = "officer_patil"
                        ),
                        Officer(
                            id = "off_2",
                            name = BilingualText("Mrs. Sneha Lata Joshi", "श्रीमती स्नेहा लता जोशी", "श्रीमती स्नेहा लता जोशी"),
                            designation = BilingualText("Deputy Collector (Revenue)", "उप कलेक्टर (राजस्व)", "उपजिल्हाधिकारी (महसूल)"),
                            room = "Cabin 104, First Floor",
                            status = PresenceStatus.PRESENT,
                            lastUpdated = BilingualText("Updated 2m ago", "2 मिनट पहले अपडेट किया गया", "२ मिनिटांपूर्वी अपडेट केले"),
                            drawableName = "officer_joshi"
                        )
                    ),
                    services = listOf(
                        Service(
                            id = "domicile",
                            name = BilingualText("Domicile Certificate", "अधिवास (मूल निवासी) प्रमाण पत्र", "अधिवास (रहवासी) प्रमाणपत्र"),
                            description = BilingualText(
                                "Proof of permanent residence within the state, required for admissions and jobs.",
                                "राज्य के भीतर स्थायी निवास का प्रमाण, प्रवेश और नौकरियों के लिए आवश्यक।",
                                "राज्यात कायमस्वरूपी वास्तव्याचा पुरावा, प्रवेश आणि नोकरीसाठी आवश्यक."
                            ),
                            estimatedMinutes = 15,
                            documentsRequired = listOf(
                                BilingualText("Aadhaar Card (identity proof)", "आधार कार्ड (पहचान प्रमाण)", "आधार कार्ड (ओळख पुरावा)"),
                                BilingualText("Ration Card / Electricity Bill (address proof)", "राशन कार्ड / बिजली बिल (पता प्रमाण)", "रेशन कार्ड / वीज बिल (पत्ता पुरावा)"),
                                BilingualText("School Leaving Certificate (birth place proof)", "स्कूल छोड़ने का प्रमाण पत्र (जन्म स्थान प्रमाण)", "शाळा सोडल्याचा दाखला (जन्मस्थळ पुरावा)"),
                                BilingualText("Affidavit of Residence", "निवास का हलफनामा (शपथ पत्र)", "रहवासी प्रतिज्ञापत्र")
                            ),
                            routeSteps = listOf(
                                RouteStep(
                                    stepNumber = 1,
                                    title = BilingualText("Enter the Main Gate", "मुख्य द्वार में प्रवेश करें", "मुख्य प्रवेशद्वारातून प्रवेश करा"),
                                    description = BilingualText("Enter through the main arch gate and undergo security screening.", "मुख्य मेहराबदार प्रवेश द्वार से अंदर जाएं और सुरक्षा जांच कराएं।", "मुख्य कमानीच्या प्रवेशद्वारातून प्रवेश करा आणि सुरक्षा तपासणी पूर्ण करा."),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 2,
                                    title = BilingualText("Go to the Central Lobby", "केंद्रीय लॉबी में जाएं", "मध्यवर्ती लॉबीमध्ये जा"),
                                    description = BilingualText("Walk 30 meters straight into the lobby. You will see a directory board.", "लॉबी में सीधे 30 मीटर चलें। आपको एक निर्देश बोर्ड दिखाई देगा।", "लॉबीमध्ये ३० मीटर सरळ चाला. तुम्हाला निर्देश फलक दिसेल."),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 3,
                                    title = BilingualText("Turn Right into Block A", "ब्लॉक ए में दाएं मुड़ें", "ब्लॉक ए मध्ये उजवीकडे वळा"),
                                    description = BilingualText("Turn right from the help desk. Walk past the waiting area.", "सहायता डेस्क से दाईं ओर मुड़ें। प्रतीक्षालय के पास से आगे चलें।", "मदत कक्षाकडून उजवीकडे वळा. प्रतीक्षा कक्षाच्या पुढे चाला."),
                                    directionIcon = "right"
                                ),
                                RouteStep(
                                    stepNumber = 4,
                                    title = BilingualText("Counter 3 on the Left", "बाईं ओर काउंटर 3", "डावीकडील काउंटर ३"),
                                    description = BilingualText("Go to Counter 3 (Revenue Services). Submit your file here.", "काउंटर 3 (राजस्व सेवाएं) पर जाएं। अपनी फाइल यहां जमा करें।", "काउंटर ३ (महसूल सेवा) वर जा. तुमची फाईल येथे सबमिट करा."),
                                    directionIcon = "left"
                                )
                            )
                        ),
                        Service(
                            id = "income",
                            name = BilingualText("Income Certificate", "आय प्रमाण पत्र", "उत्पन्नाचा दाखला"),
                            description = BilingualText(
                                "Official certificate showing annual income, useful for scholarships and fee waivers.",
                                "वार्षिक आय दर्शाने वाला आधिकारिक प्रमाण पत्र, छात्रवृत्ति और शुल्क छूट के लिए उपयोगी।",
                                "वार्षिक उत्पन्न दर्शवणारे अधिकृत प्रमाणपत्र, शिष्यवृत्ती आणि शुल्क सवलतीसाठी उपयुक्त."
                            ),
                            estimatedMinutes = 20,
                            documentsRequired = listOf(
                                BilingualText("Aadhaar Card", "आधार कार्ड", "आधार कार्ड"),
                                BilingualText("Ration Card", "राशन कार्ड", "रेशन कार्ड"),
                                BilingualText("Salary Slip (if employed) or Land Revenue Receipt", "वेतन पर्ची (यदि कार्यरत हैं) या भू-राजस्व रसीद", "पगार पत्रक (नोकरीवर असल्यास) किंवा शेतसारा पावती"),
                                BilingualText("Declaration of Income signed by Talathi / Patwari", "तलाठी / पटवारी द्वारा हस्ताक्षरित आय की घोषणा", "तलाठी / पटवारी यांनी स्वाक्षरी केलेले उत्पन्न घोषणापत्र")
                            ),
                            routeSteps = listOf(
                                RouteStep(
                                    stepNumber = 1,
                                    title = BilingualText("Enter the Main Gate", "मुख्य द्वार में प्रवेश करें", "मुख्य प्रवेशद्वारातून प्रवेश करा"),
                                    description = BilingualText("Enter and pass the security check at the entrance.", "प्रवेश द्वार पर प्रवेश करें और सुरक्षा जांच पास करें।", "प्रवेशद्वारावर प्रवेश करा आणि सुरक्षा तपासणी पूर्ण करा."),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 2,
                                    title = BilingualText("Head to First Floor Stairs", "पहली मंजिल की सीढ़ियों की ओर बढ़ें", "पहिल्या मजल्यावरील जिनाकडे जा"),
                                    description = BilingualText("Walk straight and take the stairs on the left behind the elevator.", "सीधे चलें और लिफ्ट के पीछे बाईं ओर की सीढ़ियों से जाएं।", "सरळ चाला आणि लिफ्टच्या मागे डावीकडील जिन्याने जा."),
                                    directionIcon = "up_stairs"
                                ),
                                RouteStep(
                                    stepNumber = 3,
                                    title = BilingualText("Go to Room 104", "कमरा नंबर 104 में जाएं", "केबिन १०४ मध्ये जा"),
                                    description = BilingualText("At the top of the stairs, turn left and walk 15 meters. Room 104 is on your right.", "सीढ़ियों के शीर्ष पर, बाईं ओर मुड़ें और 15 मीटर चलें। कमरा 104 आपकी दाईं ओर है।", "जिना चढल्यावर डावीकडे वळा आणि १५ मीटर चाला. केबिन १०४ तुमच्या उजवीकडे आहे."),
                                    directionIcon = "right"
                                )
                            )
                        )
                    )
                ),
                Department(
                    id = "land_records",
                    name = BilingualText("Land Records (Bhumi Abhilekh)", "भूमि रिकॉर्ड (भूमी अभिलेख)", "भूमी अभिलेख विभाग"),
                    officers = listOf(
                        Officer(
                            id = "off_3",
                            name = BilingualText("Mr. Anand P. Shinde", "श्री आनंद पी. शिंदे", "श्री. आनंद पी. शिंदे"),
                            designation = BilingualText("Superintendent of Land Records", "भूमि रिकॉर्ड के अधीक्षक", "भूमी अभिलेख अधीक्षक"),
                            room = "Cabin 205, Second Floor",
                            status = PresenceStatus.ABSENT,
                            lastUpdated = BilingualText("Updated 1h ago", "1 घंटे पहले अपडेट किया गया", "१ तासापूर्वी अपडेट केले"),
                            drawableName = "officer_shinde"
                        )
                    ),
                    services = listOf(
                        Service(
                            id = "extract_712",
                            name = BilingualText("7/12 Extract (७/१२ उतारा)", "७/१२ उतारा (भूमि रिकॉर्ड)", "७/१२ उतारा (जमीन अभिलेख)"),
                            description = BilingualText(
                                "Document containing details of land ownership, cultivation, and agricultural loans.",
                                "भूमि स्वामित्व, खेती और कृषि ऋण का विवरण देने वाला दस्तावेज।",
                                "जमिनीची मालकी, वहिवाट आणि कृषी कर्जाचा तपशील देणारा दस्तऐवज."
                            ),
                            estimatedMinutes = 10,
                            documentsRequired = listOf(
                                BilingualText("Survey Number / Gut Number of the Land", "भूमि का सर्वेक्षण नंबर / गुट नंबर", "जमिनीचा सर्वे नंबर / गट नंबर"),
                                BilingualText("Aadhaar Card of the applicant", "आवेदक का आधार कार्ड", "अर्जदाराचे आधार कार्ड"),
                                BilingualText("Application Form", "आवेदन पत्र", "अर्ज नमुना")
                            ),
                            routeSteps = listOf(
                                RouteStep(
                                    stepNumber = 1,
                                    title = BilingualText("Enter the Main Gate", "मुख्य द्वार में प्रवेश करें", "मुख्य प्रवेशद्वारातून प्रवेश करा"),
                                    description = BilingualText("Enter through the main gate.", "मुख्य द्वार से प्रवेश करें।", "मुख्य प्रवेशद्वारातून प्रवेश करा."),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 2,
                                    title = BilingualText("Take the Elevator", "लिफ्ट लें", "लिफ्ट घ्या"),
                                    description = BilingualText("Walk straight, the elevator is in the center of the lobby. Select Floor 2.", "सीधे चलें, लिफ्ट लॉबी के केंद्र में है। मंजिल 2 चुनें।", "सरळ चाला, लिफ्ट लॉबीच्या मध्यभागी आहे. मजला २ निवडा."),
                                    directionIcon = "elevator"
                                ),
                                RouteStep(
                                    stepNumber = 3,
                                    title = BilingualText("Go to Land Records Section", "भूमि रिकॉर्ड अनुभाग में जाएं", "भूमी अभिलेख विभागात जा"),
                                    description = BilingualText("Exit the elevator, turn left, and enter Room 205 at the end of the hall.", "लिफ्ट से बाहर निकलें, बाईं ओर मुड़ें, and हॉल के अंत में कमरा 205 में प्रवेश करें।", "लिफ्टमधून बाहेर पडा, डावीकडे वळा आणि हॉलच्या शेवटी केबिन २०५ मध्ये प्रवेश करा."),
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
            name = BilingualText("Municipal Corporation", "महानगरपालिका / नगर निगम", "महानगरपालिका"),
            description = BilingualText(
                "City civic center handling birth/death registration, property tax, and water supply issues.",
                "शहर का नागरिक केंद्र जो जन्म/मृत्यु पंजीकरण, संपत्ति कर और जलापूर्ति के मुद्दों को संभालता है।",
                "जन्म/मृत्यू नोंदणी, मालमत्ता कर आणि पाणीपुरवठा समस्या हाताळणारे शहर नागरी केंद्र."
            ),
            address = BilingualText("Mahatma Gandhi Road, Opp. Central Park", "महात्मा गांधी रोड, सेंट्रल पार्क के सामने", "महात्मा गांधी रोड, सेंट्रल पार्क समोर"),
            latitude = 21.1498,
            longitude = 79.0806,
            drawableName = "building_municipal",
            departments = listOf(
                Department(
                    id = "health",
                    name = BilingualText("Health & Vital Statistics", "स्वास्थ्य एवं जन्म-मृत्यू विभाग", "आरोग्य आणि जन्म-मृत्यू नोंदणी विभाग"),
                    officers = listOf(
                        Officer(
                            id = "off_4",
                            name = BilingualText("Mr. Vinay D. Deshmukh", "श्री विनय डी. देशमुख", "श्री. विनय डी. देशमुख"),
                            designation = BilingualText("Registrar of Births & Deaths", "जन्म और मृत्यु के रजिस्ट्रार", "जन्म-मृत्यू निबंधक"),
                            room = "Room 12, Ground Floor",
                            status = PresenceStatus.PRESENT,
                            lastUpdated = BilingualText("Updated 5m ago", "5 मिनट पहले अपडेट किया गया", "५ मिनिटांपूर्वी अपडेट केले"),
                            drawableName = "officer_deshmukh"
                        )
                    ),
                    services = listOf(
                        Service(
                            id = "birth_cert",
                            name = BilingualText("Birth Certificate", "जन्म प्रमाण पत्र", "जन्म प्रमाणपत्र"),
                            description = BilingualText(
                                "Official registration of a child's birth, essential for school admissions.",
                                "बच्चे के जन्म का आधिकारिक पंजीकरण, स्कूल में प्रवेश के लिए आवश्यक।",
                                "बालकाच्या जन्माची अधिकृत नोंदणी, शाळेत प्रवेशासाठी आवश्यक."
                            ),
                            estimatedMinutes = 25,
                            documentsRequired = listOf(
                                BilingualText("Hospital Discharge Card / Birth Report", "अस्पताल डिस्चार्ज कार्ड / जन्म रिपोर्ट", "रुग्णालय डिस्चार्ज कार्ड / जन्म अहवाल"),
                                BilingualText("Aadhaar Card of Mother and Father", "माता और पिता का आधार कार्ड", "माता आणि पित्याचे आधार कार्ड"),
                                BilingualText("Marriage Certificate (Optional)", "विवाह प्रमाण पत्र (वैकल्पिक)", "विवाह प्रमाणपत्र (वैकल्पिक)")
                            ),
                            routeSteps = listOf(
                                RouteStep(
                                    stepNumber = 1,
                                    title = BilingualText("Enter Civic Center Hall", "नागरिक केंद्र हॉल में प्रवेश करें", "नागरी केंद्र हॉलमध्ये प्रवेश करा"),
                                    description = BilingualText("Walk past the reception desk near the entrance.", "प्रवेश द्वार के पास रिसेप्शन डेस्क से आगे चलें।", "प्रवेशद्वाराजवळील रिसेप्शन डेस्कच्या पुढे चाला."),
                                    directionIcon = "straight"
                                ),
                                RouteStep(
                                    stepNumber = 2,
                                    title = BilingualText("Go to Health Section Corridor", "स्वास्थ्य अनुभाग कॉरिडोर में जाएं", "आरोग्य विभाग कॉरिडॉरमध्ये जा"),
                                    description = BilingualText("Turn left at the reception, walk down the corridor to Room 12.", "रिसेप्शन पर बाईं ओर मुड़ें, कॉरिडोर से होते हुए कमरा 12 में जाएं।", "रिसेप्शनवर डावीकडे वळा, कॉरिडॉरमधून केबिन १२ कडे जा."),
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
