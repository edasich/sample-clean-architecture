package com.github.edasich.authentication.data.local

import com.github.edasich.authentication.data.mapper.internal.ApplicationSessionMapper
import com.github.edasich.base.data.local.ApplicationSessionProvider
import com.github.edasich.base.data.local.model.ApplicationSession
import javax.inject.Inject

class ApplicationSessionProviderImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val applicationSessionMapper: ApplicationSessionMapper
) : ApplicationSessionProvider {

    override fun provideSession(): ApplicationSession {
        return localDataSource.getUserSession().let {
            applicationSessionMapper.mapToApplicationSession(entity = it)
        }
    }

}