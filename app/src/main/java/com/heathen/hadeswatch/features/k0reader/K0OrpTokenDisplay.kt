package com.heathen.hadeswatch.features.k0reader

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.heathen.k0r34d3r.core.K0R34D3RChunker
import com.heathen.hadeswatch.core.theme.MutedText
import com.heathen.hadeswatch.core.theme.PomegranateRed
import com.heathen.hadeswatch.core.theme.SignalCyan

@Composable
fun K0OrpTokenDisplay(
    token: String,
    fontSize: TextUnit,
    orpEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    if (token.isBlank()) {
        Text(
            text = "Load text to begin",
            style = MaterialTheme.typography.displayMedium.copy(fontSize = fontSize),
            color = MutedText,
            textAlign = TextAlign.Center,
            modifier = modifier,
        )
        return
    }

    if (!orpEnabled || token.length <= 2) {
        Text(
            text = token,
            style = MaterialTheme.typography.displayLarge.copy(fontSize = fontSize),
            color = SignalCyan,
            textAlign = TextAlign.Center,
            modifier = modifier,
        )
        return
    }

    val orpIndex = K0R34D3RChunker.optimalRecognitionPoint(token).coerceIn(0, token.lastIndex)
    val annotated = buildAnnotatedString {
        if (orpIndex > 0) {
            withStyle(SpanStyle(color = MutedText)) {
                append(token.substring(0, orpIndex))
            }
        }
        withStyle(SpanStyle(color = PomegranateRed)) {
            append(token[orpIndex].toString())
        }
        if (orpIndex < token.lastIndex) {
            withStyle(SpanStyle(color = MutedText)) {
                append(token.substring(orpIndex + 1))
            }
        }
    }
    Text(
        text = annotated,
        style = MaterialTheme.typography.displayLarge.copy(fontSize = fontSize),
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}
