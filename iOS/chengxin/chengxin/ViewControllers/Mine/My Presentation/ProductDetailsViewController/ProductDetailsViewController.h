//
//  ProductDetailsViewController.h
//  chengxin
//
//  Created by common on 7/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ProductDetailsViewController : UIViewController <UIAlertViewDelegate>

@property (nonatomic) NSDictionary* productDetail;





@property (weak, nonatomic) IBOutlet UIScrollView *slideCommerceScrollView;
@property (weak, nonatomic) IBOutlet UITextView *lblInfo;
@property (weak, nonatomic) IBOutlet UIView *viewDetail;
@property (weak, nonatomic) IBOutlet UIPageControl *slideCommercePageCtrl;
@property (weak, nonatomic) IBOutlet UIButton *btnBuy;
@property (weak, nonatomic) IBOutlet UIButton *btnFavorite;
@property (retain, nonatomic) IBOutlet UIView *noNetworkView;
@property (nonatomic, weak) IBOutlet UILabel *lblName;
@property (nonatomic, weak) IBOutlet UILabel *lblPrice;
@property (nonatomic, weak) IBOutlet UILabel *lblCode;
@property (nonatomic, weak) IBOutlet UILabel *lblNetwork;
@property (nonatomic, weak) IBOutlet UILabel *lblAddress;
@property (nonatomic, weak) IBOutlet UILabel *lblEntername;
@property (nonatomic, weak) IBOutlet UILabel *lblEntercode;
@property (nonatomic, weak) IBOutlet UILabel *lblViewcnt;
@property (nonatomic, weak) IBOutlet UIImageView *imgEnterLogo;
@property (nonatomic, weak) IBOutlet UILabel *lblEnterKindName;
@property (nonatomic, weak) IBOutlet UILabel *lblStatus;
@property (weak, nonatomic) IBOutlet UIButton *btnEdit;
@property (weak, nonatomic) IBOutlet UIButton *btnDelete;
@property (weak, nonatomic) IBOutlet UIButton *btnUpDown;
@property (weak, nonatomic) IBOutlet UILabel* lblMainProduct;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint* mainProductWidth;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint* mainProductRightMargin;

-(IBAction)onBack:(id)sender;
-(IBAction)onUpDown:(id)sender;
-(IBAction)onDelete:(id)sender;
-(IBAction)onEdit:(id)sender;
@end
