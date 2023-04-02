package eg.gov.iti.skyscanner.favourite.view

import eg.gov.iti.skyscanner.models.FavModel

interface OnClickInterface {
    fun onRowClicked(favModel: FavModel)
    fun onDeleteIconClicked(favModel: FavModel)
}