package eg.gov.iti.skyscanner.alert.view

import eg.gov.iti.skyscanner.models.UserAlerts

interface OnClickAlertInterface {
    fun deleteOnClick(userAlert: UserAlerts)
}