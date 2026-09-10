import WidgetKit
import SwiftUI

// MARK: - Color Palette
private let crimsonColor = Color(red: 0.55, green: 0.12, blue: 0.12)  // Merah Marun Khidmat (#8B1E1E)
private let charcoalColor = Color(red: 0.11, green: 0.10, blue: 0.09) // Charcoal Text (#1C1917)
private let mutedSlateColor = Color(red: 0.40, green: 0.38, blue: 0.36) // Slate Muted (#66615E)
private let creamBgColor = Color(red: 0.98, green: 0.97, blue: 0.95) // Krem Kertas Khidmat (#FAF7F2)

// MARK: - iOS 17+ Container Background Helper
extension View {
    @ViewBuilder
    func widgetContainerBackground(_ color: Color = creamBgColor) -> some View {
        if #available(iOS 17.0, *) {
            self.containerBackground(color, for: .widget)
        } else {
            self.background(color)
        }
    }
}

// MARK: - Robithoh Widget Entry
struct RobithohEntry: TimelineEntry {
    let date: Date
    let prayerName: String
    let prayerTime: String
    let locationName: String
    let tasbihCount: Int
    let tasbihTarget: Int
    let quranSurah: String
    let quranAyah: Int
}

// MARK: - Widget Timeline Provider
struct RobithohProvider: TimelineProvider {
    func placeholder(in context: Context) -> RobithohEntry {
        RobithohEntry(
            date: Date(),
            prayerName: "Subuh",
            prayerTime: "04:32",
            locationName: "Ciamis",
            tasbihCount: 33,
            tasbihTarget: 165,
            quranSurah: "Al-Fatihah",
            quranAyah: 1
        )
    }

    func getSnapshot(in context: Context, completion: @escaping (RobithohEntry) -> Void) {
        completion(placeholder(in: context))
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<RobithohEntry>) -> Void) {
        let entry = placeholder(in: context)
        let nextUpdate = Calendar.current.date(byAdding: .hour, value: 1, to: Date()) ?? Date()
        let timeline = Timeline(entries: [entry], policy: .after(nextUpdate))
        completion(timeline)
    }
}

// MARK: - Prayer Widget Entry View
struct PrayerWidgetEntryView: View {
    var entry: RobithohProvider.Entry

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack {
                Text("📿 Robithoh")
                    .font(.caption)
                    .fontWeight(.bold)
                    .foregroundColor(crimsonColor)
                Spacer()
                Text(entry.locationName)
                    .font(.caption2)
                    .fontWeight(.medium)
                    .foregroundColor(mutedSlateColor)
            }

            Spacer()

            Text("Sholat Berikutnya")
                .font(.caption2)
                .fontWeight(.medium)
                .foregroundColor(mutedSlateColor)

            HStack {
                Text(entry.prayerName)
                    .font(.headline)
                    .fontWeight(.bold)
                    .foregroundColor(charcoalColor)
                Spacer()
                Text(entry.prayerTime)
                    .font(.title3)
                    .fontWeight(.heavy)
                    .foregroundColor(crimsonColor)
            }
        }
        .environment(\.colorScheme, .light)
        .widgetContainerBackground()
    }
}

// MARK: - Tasbih Widget Entry View
struct TasbihWidgetEntryView: View {
    var entry: RobithohProvider.Entry

    var body: some View {
        VStack(spacing: 8) {
            Text("Tasbih Digital")
                .font(.caption)
                .fontWeight(.bold)
                .foregroundColor(crimsonColor)

            Text("\(entry.tasbihCount) / \(entry.tasbihTarget)")
                .font(.title2)
                .fontWeight(.heavy)
                .foregroundColor(charcoalColor)

            ProgressView(value: Double(entry.tasbihCount), total: Double(entry.tasbihTarget))
                .accentColor(crimsonColor)
        }
        .environment(\.colorScheme, .light)
        .widgetContainerBackground()
    }
}

// MARK: - Quran Widget Entry View
struct QuranWidgetEntryView: View {
    var entry: RobithohProvider.Entry

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("Terakhir Dibaca")
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(mutedSlateColor)

            Text("Surat \(entry.quranSurah)")
                .font(.subheadline)
                .fontWeight(.bold)
                .foregroundColor(charcoalColor)

            Text("Ayat \(entry.quranAyah)")
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundColor(crimsonColor)
        }
        .environment(\.colorScheme, .light)
        .widgetContainerBackground()
    }
}

// MARK: - Main Widgets Configuration
struct RobithohPrayerWidget: Widget {
    let kind: String = "PrayerWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: RobithohProvider()) { entry in
            PrayerWidgetEntryView(entry: entry)
        }
        .configurationDisplayName("Jadwal Sholat")
        .description("Menampilkan waktu sholat berikutnya dan lokasi.")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

struct RobithohTasbihWidget: Widget {
    let kind: String = "TasbihWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: RobithohProvider()) { entry in
            TasbihWidgetEntryView(entry: entry)
        }
        .configurationDisplayName("Tasbih Digital")
        .description("Menampilkan hitungan tasbih dan target wirid.")
        .supportedFamilies([.systemSmall])
    }
}

struct RobithohQuranWidget: Widget {
    let kind: String = "QuranWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: RobithohProvider()) { entry in
            QuranWidgetEntryView(entry: entry)
        }
        .configurationDisplayName("Penanda Baca Al-Qur'an")
        .description("Menampilkan surat dan ayat terakhir dibaca.")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}
