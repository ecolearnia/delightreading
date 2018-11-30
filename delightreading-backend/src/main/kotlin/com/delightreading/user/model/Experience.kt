package com.delightreading.user.model

import java.io.Serializable

data class Experience(
    var kind: String, // education, project, competition, hackathon,
    var institution: String? = null, // Weekend Hackathon Org.
    var location: String? = null,// Boston
    var title: String, // E.g. Participated in 12th Weekend Hackathon
    var subject: String? = null, // E.g. Entrepreneurship
    var description: String? = null,
    var achievements: String? = null, // 1st place.
    var fromDate: String? = null, // 20150200 some day in February 2015
    var toDate: String? = null
): Serializable {
}