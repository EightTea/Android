package com.bside.five.model

import android.net.Uri

data class GalleryInfo(var uri: Uri, var fileName: String = "", var isCheck: Boolean = false)