package com.example.hikingtimepredictor

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HikingTimePredictor(context: Context, modelPath: String) {

    private var interpreter: Interpreter
    private var mean: FloatArray
    private var scale: FloatArray

    init {
        interpreter = Interpreter(loadModelFile(context, modelPath))
        mean = loadArrayFromTxt(context, "scaler_mean.txt")
        scale = loadArrayFromTxt(context, "scaler_scale.txt")
    }

    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadArrayFromTxt(context: Context, assetName: String): FloatArray {
        val inputStream = context.assets.open(assetName)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        val content = String(buffer)
        val values = content.split("\n")
        return values.filter { it.isNotEmpty() }.map { it.toFloat() }.toFloatArray()
    }

    private fun normalizeInput(input: FloatArray): FloatArray {
        val normalized = FloatArray(input.size)
        for (i in input.indices) {
            normalized[i] = (input[i] - mean[i]) / scale[i]
        }
        return normalized
    }

    fun predict(input: FloatArray): Float {
        val normalizedInput = normalizeInput(input)
        val inputBuffer = ByteBuffer.allocateDirect(normalizedInput.size * 4).order(ByteOrder.nativeOrder())
        normalizedInput.forEach { inputBuffer.putFloat(it) }
        inputBuffer.rewind()

        val output = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        interpreter.run(inputBuffer, output)
        output.rewind()
        return output.float
    }
}
