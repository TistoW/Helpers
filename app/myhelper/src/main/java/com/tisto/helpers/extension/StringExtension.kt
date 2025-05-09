package com.tisto.helpers.extension

import android.annotation.SuppressLint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

fun String.remove(string: String): String = replace(string, "")

fun String.removeComma(): String = replace(",", "")

fun String.fixPhoneNumber(): String {
    val phone = this.remove("+")
    return when {
        take(1) == "0" -> "62${phone.substring(1, phone.length)}"
        take(2) == "62" -> phone
        isNullOrEmpty() -> phone
        else -> "62$phone"
    }
}

fun String?.uppercaseFirstChar(): String {
    val value = this?.lowercase()
    return value?.replaceFirstChar { it.uppercaseChar() } ?: ""
}

@SuppressLint("SimpleDateFormat")
fun String.convertTanggal(
    toFormat: String,
    fromFormat: String = "yyyy-MM-dd kk:mm:ss"
): String {
    if (this.contains("Z", ignoreCase = true)) {
        return this.convertFromUTC(toFormat = toFormat)
    }
    
    val inputFormat = SimpleDateFormat(fromFormat)
    val outputFormat = SimpleDateFormat(toFormat)
    return try {
        val date = inputFormat.parse(this) ?: dateExample
        outputFormat.format(date)
    } catch (e: Exception) {
        outputFormat.format(inputFormat.parse(dateExample) ?: dateExample)
    }
}

fun String.convertDate(
    toFormat: String,
    fromFormat: String = "yyyy-MM-dd kk:mm:ss"
): String {
    return this.convertTanggal(toFormat, fromFormat)
}


const val defaultDateFormat = "yyyy-MM-dd kk:mm:ss"
const val defaultDateFormatMillisecond = "yyyy-MM-dd kk:mm:ss.SSS"
const val dateExampleUTC = "1990-01-01T00:00:00.000000Z"
const val dateExample = "1990-01-01 00:00:00"
const val defaultUTCDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

@SuppressLint("SimpleDateFormat")
fun String.convertToUTC(
    fromFormat: String = defaultDateFormat,
    addDay: Int? = null
): String {
    val newFormat = defaultUTCDateFormat
    val dateFormat = SimpleDateFormat(fromFormat)
    var result = dateExampleUTC
    try {
        result = if (addDay.isNotNull()) {
            val dd = dateFormat.parse(this)
            // tambah 7 jam untuk indonesia
            val hour: Long = 3600 * 1000 // in milli-seconds.
            val newDate = Date((dd?.time.def(1)) + (addDay.def(1)) * hour)
            dateFormat.applyPattern(newFormat)
            dateFormat.format(newDate)
        } else {
            val date = dateFormat.parse(this)
            dateFormat.applyPattern(newFormat)
            dateFormat.format(date ?: dateExample)
        }
    } catch (e: ParseException) {
        loge(e.message)
    }
    return result.replace(" 24", " 00")
}

@SuppressLint("SimpleDateFormat")
fun dummyResult(toFormat: String): String {
    val dateFormat = SimpleDateFormat(defaultDateFormat)
    val date = dateFormat.parse(dateExample)
    dateFormat.applyPattern(toFormat)
    return dateFormat.format(date ?: dateExample)
}

@SuppressLint("SimpleDateFormat")
fun String?.convertFromUTC(
    toFormat: String = defaultDateFormat,
    fromFormat: String = defaultUTCDateFormat,
    timeZone: String = "Asia/Jakarta"
): String {
    if (this == null) return dummyResult(toFormat)
    var result: String
    try {
        val utcFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
        val indonesianFormat = SimpleDateFormat(toFormat, Locale.getDefault())
        indonesianFormat.timeZone = TimeZone.getTimeZone(timeZone)
        val utcDate = utcFormat.parse(this)
        result = utcDate?.let { indonesianFormat.format(it) } ?: "2000-01-01 01:00:00"
    } catch (e: Exception) {
        result = dummyResult(toFormat)
        logs("Error Time Format:${e.message}")
    }
    return result.replace(" 24", " 00")
}

fun String.convertFromUTCDay(): String {
    return this.convertFromUTCDayTime(false)
}

@SuppressLint("SimpleDateFormat")
fun String.convertFromUTCDayTime(time: Boolean = true): String {
    val date = this.convertFromUTC()

    var tanggal = ""
    var hari = ""

    val formatTgl = "dd MMM yyyy${if (time) " kk:mm" else ""}"
    val formatHari = "EEEE"
    val formatLama = defaultDateFormat

    val dateFormat = SimpleDateFormat(formatLama)
    val dateFormat2 = SimpleDateFormat(formatLama)
    try {
        val dd = dateFormat.parse(date)
        dateFormat.applyPattern(formatTgl)
        tanggal = dateFormat.format(dd!!)

        val mHari = dateFormat2.parse(date)
        dateFormat2.applyPattern(formatHari)
        hari = dateFormat2.format(mHari!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    when (hari) {
        "Sunday" -> hari = "Minggu"
        "Monday" -> hari = "Senin"
        "Tuesday" -> hari = "Selasa"
        "Wednesday" -> hari = "Rabo"
        "Thursday" -> hari = "Kamis"
        "Friday" -> hari = "Jumat"
        "Saturday" -> hari = "Sabtu"
    }

    return "$hari, $tanggal${if (time) " WIB" else ""}"
}

fun Int?.toRupiah(hideCurrency: Boolean = false): String {
    return (this ?: 0).formatCurrency(!hideCurrency)
}

fun Double?.toRupiah(hideCurrency: Boolean = false): String {
    return (this ?: 0.0).formatCurrency(!hideCurrency)
}

fun String?.toRupiah(hideCurrency: Boolean = false): String {
    return (this ?: "0").formatCurrency(!hideCurrency)
}

fun String.getYoutubeId(): String {
    return when {
        this.contains("youtu.be") -> this.split("/")[3]
        this.contains("youtube.com") -> this.substringAfter("watch?v=").substringBefore("&")
        else -> {
            this
        }
    }
}

fun String.toRequestBody(): RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun Int.toRequestBody(): RequestBody {
    return this.toString().toRequestBody("text/plain".toMediaTypeOrNull())
}

@SuppressLint("SimpleDateFormat")
fun String.toSalam(): String {
    val dateNow = System.currentTimeMillis()
    val sTgl = SimpleDateFormat("dd MMMM yyyy")
    val sJam = SimpleDateFormat("kk")
    val tgl: String = sTgl.format(dateNow)
    val jam: String = sJam.format(dateNow)

    val iJam = jam.toInt()
    var salam = ""
    if (iJam <= 10) salam = "Selamat Pagi"
    if (iJam in 11..14) salam = "Selamat Siang"
    if (iJam in 13..18) salam = "Selamat Sore"
    if (iJam in 19..24) salam = "Selamat Malam"
    return salam
}

fun String?.getInitial(): String {
    try {
        if (this.isNullOrEmpty()) return ""
        val array = this.split(" ")
        if (array.isEmpty()) return this
        var inisial = array[0].substring(0, 1)
        if (array.size > 1) inisial += array[1].substring(0, 1)
        return inisial.uppercase()
    } catch (e: Exception) {
        return "N"
    }
}

fun String?.toKFormat(): String {
    if (this == null) return ""
    return if (this.length > 4) {
        return when (this.length) {
            4 -> this.toRupiah(true).dropLast(2) + "K"
            in 5..6 -> this.dropLast(3) + "K"
            7 -> this.toRupiah(true).dropLast(6) + "M"
            in 8..9 -> this.dropLast(6) + "M"
            10 -> this.toRupiah(true).dropLast(6) + "M"
            in 11..100 -> this.dropLast(9).toRupiah(true) + "B"
            else -> this
        }
    } else this
}

fun String.searchQuery(): String {
    return "%$this%"
}

fun getRandomName(withNumber: Boolean = false): String {
    val listName = listOf(
        "Agus Pratama",
        "Budi Santoso",
        "Citra Dewi",
        "Dedi Kusuma",
        "Eko Saputra",
        "Fitri Ayu",
        "Gita Sari",
        "Hari Susanto",
        "Indra Setiawan",
        "Joko Riyadi",
        "Kiki Ananda",
        "Lina Permata",
        "Maya Puspita",
        "Nina Sari",
        "Oka Wirawan",
        "Putu Dewi",
        "Rian Syahputra",
        "Susi Susanti",
        "Taufik Hidayat",
        "Umar Bakri",
        "Vina Melinda",
        "Wahyu Nugroho",
        "Yusuf Mansur",
        "Zahra Putri",
        "Andi Wirawan",
        "Bella Siregar",
        "Candra Wijaya",
        "Dewi Lestari",
        "Edi Supriyadi",
        "Farhan Ridwan",
        "Gilang Mahendra",
        "Hendra Gunawan",
        "Ika Susanti",
        "Joni Sugiarto",
        "Kurniawan Putra",
        "Linda Kurnia",
        "Meli Yanti",
        "Nadia Fitria",
        "Oni Siregar",
        "Pandu Wibowo",
        "Rina Amelia",
        "Siti Fadhilah",
        "Toni Andika",
        "Usman Hakim",
        "Viona Kusuma",
        "Wira Aditya",
        "Yuli Hartono",
        "Zaki Rahman",
        "Adi Pratama",
        "Benny Gunawan",
        "Cici Herlina",
        "Dian Sastrowardoyo",
        "Endang Widodo",
        "Feri Irawan",
        "Gusman Kurniawan",
        "Hani Wulandari",
        "Irfan Ramadhan",
        "Jaka Supriyadi",
        "Kartika Dewi",
        "Lutfi Rahman",
        "Mira Utami",
        "Nino Saputra",
        "Olga Maharani",
        "Via Amelia",
        "Rendi Kurnia",
        "Sandy Pratama",
        "Tirta Purnama",
        "Uli Handayani",
        "Vega Sari",
        "Wulan Anggraini",
        "Yogi Priyanto",
        "Zulfi Syahputra",
        "Asep Rahmat",
        "Beni Supriyadi",
        "Cici Purnama",
        "Dika Mahendra",
        "Eva Susanti",
        "Faisal Akbar",
        "Guntur Saputra",
        "Heryanto Wijaya",
        "Iwan Setiawan",
        "Joko Widodo",
        "Krisna Wijaya",
        "Lala Sari",
        "Meli Septiani",
        "Nadia Anggraini",
        "Oky Andika",
        "Putu Sari",
        "Rani Amalia",
        "Sigit Pratama",
        "Tina Permata",
        "Utami Sari",
        "Vino Fadillah",
        "Wahyu Hidayat",
        "Yulia Hartati",
        "Zainal Abidin"
    )
    return listName[randomInt(0, listName.size - 1)] + "" + if (withNumber) randomInt(
        10,
        99
    ) else ""
}

fun randomInt(from: Int, to: Int): Int {
    val randomGenerator = Random(System.currentTimeMillis())
    return randomGenerator.nextInt(from, to)
}

fun generateRandomName(withNumber: Boolean = false) = getRandomName(withNumber)

@SuppressLint("SimpleDateFormat")
fun getSalam(): String {
    val dateNow = System.currentTimeMillis()
    val sTgl = SimpleDateFormat("dd MMMM yyyy")
    val sJam = SimpleDateFormat("kk")
    val tgl: String = sTgl.format(dateNow)
    val jam: String = sJam.format(dateNow)

    val iJam = jam.toInt()
    var salam = ""
    if (iJam <= 10) salam = "Selamat Pagi"
    if (iJam in 11..14) salam = "Selamat Siang"
    if (iJam in 13..18) salam = "Selamat Sore"
    if (iJam in 19..24) salam = "Selamat Malam"
    return salam
}

fun String?.clearJsonString(): String {
    return this?.replace("\"{", "{")?.replace("}\"", "}")?.replace("\\", "") ?: ""
}

fun String?.equalText(string: String?): Boolean {
    return this?.lowercase()?.contains(string?.lowercase() ?: "") ?: false
}

fun String.translateJson(): String {
    return this.replace("\\u003d", "=")
}

fun Double?.formatCurrency(showCurrency: Boolean = false): String {
    if (this.isNull()) return "0"
    val localeID = Locale("in", "ID")
    val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
    formatRupiah.minimumFractionDigits = 0
    return try {
        var formated = formatRupiah
            .format(this)
            .replace("Rp. ", "Rp")
        formated = formated.replace("Rp", if (showCurrency) "Rp" else "")
        formated.replace(".", ",")
    } catch (e: Exception) {
        e.printStackTrace()
        "0"
    }
}

fun String?.formatCurrency(showCurrency: Boolean = false): String {
    return this.toDoubleSafety().formatCurrency(showCurrency)
}

fun Int?.formatCurrency(showCurrency: Boolean = false): String {
    return this.toDoubleSafety().formatCurrency(showCurrency)
}

fun String?.startWithZero(): String {
    var result = this
    if (this?.startsWith("62") == true) {
        result = "0" + result?.substring(2)
    }
    return result ?: ""
}

fun String.isEmailValid(): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(this)
}