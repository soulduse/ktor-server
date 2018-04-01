package com.dave.fish.util

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AwsS3Provider {

//    @Throws(IOException::class)
//    fun upload(multipartFile: MultipartFile): String {
//        return uploadMultipartFile(multipartFile, BUCKET_NAME)
//    }

    fun upload(file: File): String {
        val bucketName = BUCKET_NAME + ARCHIVE_DIR_NAME
        return uploadToS3(file, bucketName, file.name)
    }

//    @Throws(IOException::class)
//    private fun uploadMultipartFile(multipartFile: MultipartFile, bucketName: String): String {
//        val targetFile: File = convert(multipartFile)
//        val uploadImageUrl = uploadToS3(targetFile, bucketName, multipartFile.originalFilename)
//        removeNewFile(targetFile)
//        return uploadImageUrl
//    }

    fun removeNewFile(targetFile: File) {
        if (targetFile.delete()) {
            // 파일 삭제
        } else {
            // 파일 삭제 실패
        }
    }
//
//    @Throws(IOException::class)
//    private fun convert(file: MultipartFile): File {
//        val convertFile = File(file.originalFilename)
//        convertFile.createNewFile()
//        try {
//            val fos = FileOutputStream(convertFile)
//            fos.write(file.bytes)
//        } catch (e: IOException) {
//            throw e
//        }
//        return convertFile
//    }

    fun uploadToS3(uploadFile: File, bucketName: String, fileName: String): String {
        val s3 = getS3Instance(getAwsCredentials())
        return putS3(uploadFile, bucketName, fileName, s3)
    }

    private fun getAwsCredentials(): AWSCredentials {
        val credentials: AWSCredentials
        try {
            credentials = ProfileCredentialsProvider().credentials
        } catch (e: Exception) {
            throw AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e)
        }
        return credentials
    }

    private fun getS3Instance(credentials: AWSCredentials): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(AWSStaticCredentialsProvider(credentials))
                .withRegion("ap-northeast-2")
                .build()
    }

    private fun putS3(uploadFile: File, bucketName: String, fileName: String, s3: AmazonS3): String {
        try {
            s3.putObject(PutObjectRequest(bucketName, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead))
            return s3.getUrl(bucketName, fileName).toString()
        } catch (ase: AmazonServiceException) {
            throw ase
        } catch (ace: AmazonClientException) {
            throw ace
        }
    }

    companion object {
        const val BUCKET_NAME = "fishprject"
        const val ARCHIVE_DIR_NAME = "board"
    }
}
