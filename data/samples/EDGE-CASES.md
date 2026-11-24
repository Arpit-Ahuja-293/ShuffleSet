# Edge case samples

Columns: 19 Exportify columns + `BPM`, `Camelot`, `Energy` (0-100).
Real songs use their Tunebat values from `main.csv`, except the one field each row deliberately breaks.
Rows 2, 3, 4 and 12 are fictional test tracks with made-up values.

## edge-cases.csv

| Row | Track | Case | Expected |
|---|---|---|---|
| 1 | Boogie Wonderland | `\,` escaped comma in artist (`Earth\, Wind & Fire`) | Parsed; one artist field kept intact |
| 2 | Stay, Don't Go | Comma inside quoted title | Parsed; title keeps comma |
| 3 | The "Real" Mix | `""` escaped quotes in title | Parsed; title is `The "Real" Mix` |
| 4 | Café Del Mar / Röyksopp | UTF-8 accents | Parsed; accents preserved |
| 5 | Love Sensation | BPM empty | Skipped, warning |
| 6 | Good Times | BPM `abc` | Skipped, warning |
| 7 | Hollywood Swinging | BPM `0` | Skipped, warning |
| 8 | Disco Inferno | Camelot `13A` (valid is 1-12, A/B) | Skipped, warning |
| 9 | Higher Ground | Camelot `A Minor` (not Camelot format) | Skipped, warning |
| 10 | I Will Survive | Energy `150` (valid is 0-100) | Skipped, warning |
| 11 | Boogie Wonderland | Duplicate of row 1 | Skipped, warning; first kept |
| 12 | Half Time Groove | 64 BPM (half of 128) | Parsed; scoring may treat as 128 |
| 13 | La Mouche | Values with spaces, lowercase `8a` | Parsed after trim/uppercase: 131, 5A, 68 |

Valid tracks after parsing: rows 1, 2, 3, 4, 12, 13 (6 tracks).

## empty.csv

Header only, no tracks. Expected: program terminates with error "no tracks found".

## bad-header.csv

`BPM` column renamed to `Tempo`. Expected: program terminates with error naming the missing `BPM` column.
