package com.heathen.hadeswatch.features.k0reader.epub

import android.content.Context
import android.net.Uri
import com.heathen.k0r34d3r.core.epub.EpubImportException
import com.heathen.k0r34d3r.core.epub.EpubParseResult
import com.heathen.k0r34d3r.core.epub.EpubTextExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EpubDocumentLoader {

    suspend fun loadFromUri(context: Context, uri: Uri, displayName: String? = null): EpubParseResult =
        withContext(Dispatchers.IO) {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: throw EpubImportException("Could not read selected file.")
            if (bytes.isEmpty()) throw EpubImportException("Selected file is empty.")
            val name = displayName ?: uri.lastPathSegment
            try {
                EpubTextExtractor.extract(bytes, filename = name)
            } catch (e: EpubImportException) {
                throw e
            } catch (e: Exception) {
                throw EpubImportException("Unable to read EPUB. It may be corrupted or DRM-protected.")
            }
        }
}
