//
//  FavouriteListViewController.h
//  chengxin
//
//  Created by common on 4/17/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    em_FavouriteProduct,
    em_ReDian
} FavouriteList_Tab;

@interface FavouriteListViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, UICollectionViewDelegate, UICollectionViewDataSource>
{
    FavouriteList_Tab tab_selection;
}

@property (nonatomic, retain) IBOutlet UIButton* btnProduct;
@property (nonatomic, retain) IBOutlet UIButton* btnReDian;
@property (nonatomic, retain) IBOutlet UILabel* lblProductUnderline;
@property (nonatomic, retain) IBOutlet UILabel* lblReDianUnderline;

@property (nonatomic, retain) IBOutlet UICollectionView *productCollectionView;
@property (nonatomic, retain) IBOutlet UITableView *tblRedian;

@property (nonatomic, retain) IBOutlet UIView *blankView;

-(IBAction)onProduct:(id)sender;
-(IBAction)onReDian:(id)sender;
-(IBAction)onBack:(id)sender;
@end
