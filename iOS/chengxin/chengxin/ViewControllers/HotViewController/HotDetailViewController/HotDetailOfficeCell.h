//
//  HotDetailOfficeCell.h
//  chengxin
//
//  Created by seniorcoder on 11/9/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HotDetailOfficeCell : UITableViewCell

@property (nonatomic, strong) NSString *accountID;
@property (weak, nonatomic) IBOutlet UILabel *lblLogo;

@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblRatio;
@property (weak, nonatomic) IBOutlet UILabel *lblProduct;
@property (weak, nonatomic) IBOutlet UILabel *lblService;
@property (weak, nonatomic) IBOutlet UIButton *btnFavourite;
@property (weak, nonatomic) IBOutlet UIButton *btnXY;

@end
