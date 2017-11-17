//
//  FavouriteViewController.h
//  chengxin
//
//  Created by common on 7/22/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef enum {
    em_WoDeGuanZhu,
    em_ChengXinLian
} Favourite_Tab;
@interface FavouriteViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
{
    Favourite_Tab tab_selection;
}
@property (nonatomic, retain) IBOutlet UIScrollView* myFavouriteContentsView;
@property (nonatomic, retain) IBOutlet UIScrollView* chengxinlianContentsView;
@property (nonatomic, retain) IBOutlet UITableView* favouriteItemTableView;
@property (nonatomic, retain) IBOutlet UITableView* chengxinlianItemTableView;
@property (nonatomic, retain) IBOutlet UIView* contentsContainer;
@property (nonatomic, retain) IBOutlet UIButton* btnMyFavourite;
@property (nonatomic, retain) IBOutlet UIButton* btnChengXinLian;
@property (nonatomic, retain) IBOutlet UILabel* lblMyFavouriteUnderline;
@property (nonatomic, retain) IBOutlet UILabel* lblChengXinLianUnderline;

-(IBAction)onMyFavourite:(id)sender;
-(IBAction)onTrustSeries:(id)sender;
@end
