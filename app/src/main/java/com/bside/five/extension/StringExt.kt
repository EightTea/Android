package com.bside.five.extension

import java.util.regex.Pattern

/**
 *  한글 구분
 */
fun CharSequence?.isHangul(): Boolean = Pattern.matches("^[ㄱ-ㅎ가-힣0-9]*\$", this)

/**
 *  영문 구분
 */
fun CharSequence?.isEnglish(): Boolean = Pattern.matches("^[a-zA-Z\\s]+\$", this)

/**
 *  숫자 구분
 */
fun CharSequence?.isNumeric(): Boolean = Pattern.matches("-?\\d+(\\.\\d+)?", this)