package com.chandmahame.testchandmahame.util

fun compute(){
    val t=setOf<String>("2","d","j","M")
    val hashT=t.distinct().hashCode()
    val s= listOf<Set<String>>()
    val hashS=ArrayList<Int>()
    for (item in s){
        hashS.add(item.distinct().hashCode())
    }
    for (item in hashS){
        if (item == hashT.hashCode())
            print("YES")
        else
            print("NO")
    }

}

