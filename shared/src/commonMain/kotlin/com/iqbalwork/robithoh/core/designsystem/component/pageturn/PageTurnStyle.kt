package com.iqbalwork.robithoh.core.designsystem.component.pageturn

/**
 * User-configurable style for page transitions in the Quran Mushaf reader.
 */
enum class PageTurnStyle {
    /** Skeuomorphic paper curl wrapping a virtual cylinder (Default). */
    CURL,

    /** Standard horizontal sliding translation. */
    SLIDE,

    /** Instant page jump without transition animation. */
    NONE
}
