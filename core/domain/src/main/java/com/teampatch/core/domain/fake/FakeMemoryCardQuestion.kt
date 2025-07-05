package com.teampatch.core.domain.fake

import com.teampatch.core.domain.model.memory.MemoryCardQuestion

class FakeMemoryCardQuestion : FakeModel<MemoryCardQuestion>() {
    override fun build(): MemoryCardQuestion = MemoryCardQuestion(
        question = "다은이를 분만실에서 처음 봤을 때 어떤 느낌이 들었나요?",
        last = true
    )
}