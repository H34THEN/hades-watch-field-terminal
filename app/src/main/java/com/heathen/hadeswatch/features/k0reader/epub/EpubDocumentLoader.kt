package com.heathen.hadeswatch.features.k0reader.epub

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.heathen.k0r34d3r.core.epub.EpubImportException
import com.heathen.k0r34d3r.core.epub.EpubParseResult
import com.heathen.k0r34d3r.core.epub.EpubTextExtractor
import java.io.FileInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EpubDocumentLoader {

    suspend fun loadFromUri(context: Context, uri: Uri, displayName: String? = null): EpubParseResult =
        withContext(Dispatchers.IO) {
            val bytes = readUriBytes(context, uri)
            if (bytes.isEmpty()) throw EpubImportException("Selected file is empty.")
            val name = displayName ?: queryDisplayName(context, uri) ?: uri.lastPathSegment
            try {
                EpubTextExtractor.extract(bytes, filename = name)
            } catch (e: EpubImportException) {
                throw e
            } catch (e: Exception) {
                throw EpubImportException("Unable to read EPUB. It may be corrupted or DRM-protected.")
            }
        }

    private fun readUriBytes(context: Context, uri: Uri): ByteArray {
        context.contentResolver.openInputStream(uri)?.use { stream ->
            return stream.readBytes()
        }
        context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
            FileInputStream(pfd.fileDescriptor).use { stream ->
                return stream.readBytes()
            }
        }
        throw EpubImportException("Could not read selected file.")
    }

    private fun queryDisplayName(context: Context, uri: Uri): String? {
        if (uri.scheme != "content") return null
        return context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            }
    }
}
